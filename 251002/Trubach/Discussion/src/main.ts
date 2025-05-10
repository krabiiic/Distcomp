import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import {
  Client,
  ClientKafka,
  EventPattern,
  MessagePattern,
  Transport,
} from "@nestjs/microservices";

async function bootstrap() {
  const app = await NestFactory.createMicroservice(AppModule, {
    transport: Transport.KAFKA,
    options: {
      client: {
        // clientId: 
        // clientId: `consumer-${uuidv4()}`,
        brokers: ['localhost:9092'],
      },
      consumer: {
        groupId: 'discussion',
      },
    },
  },);
  await app.listen();
}
bootstrap();
