import express from 'express';
import { ReactionController } from './controllers/reaction.controller.js';
import { errorHandler } from './errorHandler.js';
import {startConsumer} from "./kafka.js";

const app = express();
app.use(express.json());

const reactionController = new ReactionController();
startConsumer();
app.post('/api/v1.0/reactions', reactionController.create.bind(reactionController));
app.get('/api/v1.0/reactions', reactionController.findAll.bind(reactionController));
app.get('/api/v1.0/reactions/:id', reactionController.findById.bind(reactionController));
app.put('/api/v1.0/reactions', reactionController.update.bind(reactionController));
app.delete('/api/v1.0/reactions/:id', reactionController.delete.bind(reactionController));

app.use(errorHandler);

app.listen(24130, () => {
    console.log('Discussion module running on http://localhost:24130');
});