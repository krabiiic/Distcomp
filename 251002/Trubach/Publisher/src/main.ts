import { NestFactory } from "@nestjs/core";
import { AppModule } from "./app.module";
import {
  Client,
  ClientKafka,
  EventPattern,
  MicroserviceOptions,
  Transport,
} from "@nestjs/microservices";

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.connectMicroservice<MicroserviceOptions>({
    transport: Transport.KAFKA,
    options: {
      client: {
        brokers: ["localhost:9092"],
      },
      consumer: {
        groupId: "publisher",
      },
    },
  });
  await app.startAllMicroservices();
  await app.listen(process.env.PORT ?? 24110);
}
bootstrap();
