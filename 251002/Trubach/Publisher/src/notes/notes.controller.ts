import {
  Body,
  Controller,
  Delete,
  Get,
  HttpCode,
  HttpException,
  Param,
  Post,
  Put,
} from "@nestjs/common";
import {
  Client,
  ClientKafka,
  ClientRedis,
  EventPattern,
  MessagePattern,
  Transport,
} from "@nestjs/microservices";
import Redis from "ioredis";
import { firstValueFrom, timeout } from "rxjs";

@Controller("/api/v1.0/notes/")
export class NotesController {
  @Client({
    transport: Transport.REDIS,
    options: {
      host: 'localhost',
      port: 6379,
    },
  })
  private client_r: ClientRedis;

  @Client({
    transport: Transport.KAFKA,
    options: {
      producer: {
        allowAutoTopicCreation: true,
      },
      consumer: {
        groupId: "discussion",
      },
      client: {
        brokers: ["localhost:9092"],
      },
    },
  })
  private client_k: ClientKafka;

  private responsePromises: Map<number, (data: any) => void> = new Map();
  private readonly CACHE_TTL = 60; // Cache time-to-live in seconds
  private readonly NOTES_CACHE_KEY = 'notes:all';
  private readonly NOTE_CACHE_PREFIX = 'note:';
   

  async onModuleInit() {
    await this.client_k.connect();

    this.client_k.emit("NOTES_IN", {}); 
    this.client_k.emit("NOTES_OUT", {}); 
    this.client_r.connect();
  }

  private createRecvHandler(): [number, Promise<any>] {
    const requestId = Date.now();
    return [
      requestId,
      new Promise((resolve) => {
        this.responsePromises.set(requestId, resolve);
      }),
    ];
  }

  async setCache(key: string, value: any, ttl: number): Promise<void> {
    const [pubClient] = this.client_r.unwrap<[Redis, Redis]>();
    await pubClient.set(key, JSON.stringify(value));
  }

  
  async delCache(key: string): Promise<void> {
    const [pubClient] = this.client_r.unwrap<[Redis, Redis]>();
    await pubClient.del(key);
  }

  async getCache(key: string): Promise<any> {
    const [pubClient] = this.client_r.unwrap<[Redis, Redis]>();
    return await (JSON.parse(await pubClient.get(key) ?? "{}") || {});
  }

  async updCache(key: string, value: any): Promise<void> {
    const [pubClient] = this.client_r.unwrap<[Redis, Redis]>();
    
    await pubClient.set(key, JSON.stringify(value));
  }

  @Post()
  async create(@Body() noteDto: any): Promise<any> {
    try {
      const [requestId, resp] = this.createRecvHandler();
      this.client_k.emit("NOTES_IN", {
        action: "create",
        data: noteDto,
        requestId,
      });
      const res = await resp;
      return res;
    } catch (error) {
      this.handleError(error);
    }
  }

  @Get()
  async findAll(): Promise<any> {
    try {
      const cached = await this.getCache(this.NOTES_CACHE_KEY);
      if (cached) {
        return cached;
      }
      const [requestId, resp] = this.createRecvHandler();
      this.client_k.emit("NOTES_IN", {
        action: "findAll",
        requestId,
      });
      console.log("SENT", requestId);
      const res = await resp;
      await this.setCache(this.NOTES_CACHE_KEY, res, this.CACHE_TTL);
      return res;
    } catch (error) {
      console.error(error);
      this.handleError(error);
    }
  }

  @Get(":id")
  async findOne(@Param("id") id: number): Promise<any> {
    try {
      // const cached = await this.getCache(this.NOTE_CACHE_PREFIX + id);
      // if (cached) {
      //   console.log("AAA",cached, id)
      //   return cached;
      // }
      const [requestId, resp] = this.createRecvHandler();
      this.client_k.emit("NOTES_IN", {
        action: "findOne",
        id: id,
        requestId,
      });
      const res = await resp;
      await this.setCache(this.NOTE_CACHE_PREFIX + id, res, this.CACHE_TTL);
      return res;
    } catch (error) {
      this.handleError(error);
    }
  }

  @Put()
  async update(@Body() noteDto: any): Promise<any> {
    try {
      // this.updCache(this.NOTE_CACHE_PREFIX + noteDto.id, noteDto);
      // this.delCache(this.NOTES_CACHE_KEY);
      const [requestId, resp] = this.createRecvHandler();
      this.client_k.emit("NOTES_IN", {
        action: "update",
        data: noteDto,
        requestId,
      });
      return resp;
    } catch (error) {
      this.handleError(error);
    }
  }

  @HttpCode(204)
  @Delete(":id")
  async delete(@Param("id") id: number): Promise<void> {
    try {
      // this.delCache(this.NOTE_CACHE_PREFIX + id);
      // this.delCache(this.NOTES_CACHE_KEY);
      const [requestId, resp] = this.createRecvHandler();
      this.client_k.emit("NOTES_IN", {
        action: "delete",
        id: id,
        requestId,
      });
      return await resp;
    } catch (error) {
      this.handleError(error);
    }
  }

  @EventPattern("NOTES_OUT")
  handleResponse(data: any) {
    console.log("RECV", data.requestId);
    const promise = this.responsePromises.get(data.requestId);
    if (promise) {
      promise(data.response);
      this.responsePromises.delete(data.requestId);
    }

    return data;
  }

  private handleError(error: any): void {
    throw new HttpException(
      error.message || "Internal Server Error",
      error.status || 500,
    );
  }
}
