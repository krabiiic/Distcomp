import express from 'express';
import { MessageController } from './controllers/message.controller.js';
import { errorHandler } from './errorHandler.js';
import {startConsumer} from "./kafka.js";

const app = express();
app.use(express.json());

const messageController = new MessageController();
startConsumer();
app.post('/api/v1.0/messages', messageController.create.bind(messageController));
app.get('/api/v1.0/messages', messageController.findAll.bind(messageController));
app.get('/api/v1.0/messages/:id', messageController.findById.bind(messageController));
app.put('/api/v1.0/messages', messageController.update.bind(messageController));
app.delete('/api/v1.0/messages/:id', messageController.delete.bind(messageController));

app.use(errorHandler);

app.listen(24130, () => {
    console.log('Discussion module running on http://localhost:24130');
});