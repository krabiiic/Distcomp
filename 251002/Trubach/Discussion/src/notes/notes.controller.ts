import {
  Controller,
} from '@nestjs/common';
import { EventPattern, Client, ClientKafka, Transport } from '@nestjs/microservices';
import { NotesService } from './notes.service';
import { NoteRequestDto, NoteResponseDto } from '../common/dto/note.dto';

@Controller()
export class NotesController {
  @Client({
    transport: Transport.KAFKA,
    options: {
      producer: {
        allowAutoTopicCreation: true,
      },
      consumer: {
        groupId: "publisher",
      },
      client: {
        brokers: ['localhost:9092'],
      }
    }
  })
  private client_k: ClientKafka;

  constructor(private readonly notesService: NotesService) {}

  async onModuleInit() {
    await this.client_k.connect();
  }

  @EventPattern('NOTES_IN')
  async handleNotesRequest(data: any) {
    console.log("RECV", data);
    try {
      let result;
      
      switch(data.action) {
        case 'create':
          result = await this.notesService.create(data.data);
          break;
        case 'findAll':
          result = await this.notesService.findAll();
          break;
        case 'findOne':
          result = await this.notesService.findOne(data.id);
          break;
        case 'update':
          result = await this.notesService.update(data.data);
          break;
        case 'delete':
          await this.notesService.delete(data.id);
          result = { success: true };
          break;
        default:
          throw new Error('Invalid action');
      }

      console.log("SENT", {
        ...data,
        response: result,
        success: true
      });
      this.client_k.emit('NOTES_OUT', {
        ...data,
        response: result,
        success: true
      });
    } catch (error) {
      this.client_k.emit('NOTES_OUT', {
        ...data,
        error: {
          message: error.message,
          status: error.status || 500
        },
        success: false
      });
    }
  }
}