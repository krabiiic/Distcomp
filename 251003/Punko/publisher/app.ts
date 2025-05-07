import express from 'express';
import 'reflect-metadata';
import { createConnection } from 'typeorm';
import { AuthorController } from './src/controllers/author.controller.js';
import { MarkerController } from './src/controllers/marker.controller.js';
import { ReactionController } from './src/controllers/reaction.controller.js';
import { IssueController } from './src/controllers/issue.controller.js';
import { AuthorRequestTo } from './src/dtos/author.dto.js';
import { validationMiddleware } from './middleware.js';
import { IssueRequestTo } from './src/dtos/issue.dto.js';
import { MarkerRequestTo } from './src/dtos/marker.dto.js';
import { ReactionRequestTo } from './src/dtos/reaction.dto.js';
import {
    authorCreateError,
    authorModifierError,
    issueCreateError,
    issueModifierError,
    markCreateError,
    markModifierError,
    reactionCreateError,
    reactionModifierError,
} from './errors.js';
import { errorHandler } from './errorHandler.js';
import {initKafka} from "./src/kafka";

const app = express();
app.use(express.json());

async function initializeApp() {
    try {
        // Устанавливаем соединение с базой данных
        await createConnection({
            type: 'postgres',
            host: 'localhost',
            port: 5432,
            username: 'postgres',
            password: 'postgres',
            database: 'distcomp',
            synchronize: true,
            entities: ['src/entities/*.ts'], // Убедитесь, что путь к сущностям указан правильно
            logging: true, // Включаем логи для отладки
        });
        console.log('Database connection established');
        initKafka();

        // Создаем экземпляры контроллеров ПОСЛЕ установления соединения
        const authorController = new AuthorController();
        const markerController = new MarkerController();
        const reactionController = new ReactionController();
        const issueController = new IssueController();

        // Роуты для Author
        app.post('/api/v1.0/authors', validationMiddleware(AuthorRequestTo, authorCreateError), authorController.create);
        app.get('/api/v1.0/authors', authorController.findAll);
        app.get('/api/v1.0/authors/:id', authorController.findById);
        app.put('/api/v1.0/authors', validationMiddleware(AuthorRequestTo, authorModifierError), authorController.update);
        app.delete('/api/v1.0/authors/:id', authorController.delete);

        // Роуты для Marker
        app.post('/api/v1.0/markers', validationMiddleware(MarkerRequestTo, markCreateError), markerController.create);
        app.get('/api/v1.0/markers', markerController.findAll);
        app.get('/api/v1.0/markers/:id', markerController.findById);
        app.put('/api/v1.0/markers', validationMiddleware(MarkerRequestTo, markModifierError), markerController.update);
        app.delete('/api/v1.0/markers/:id', markerController.delete);

        // Роуты для Reaction
        app.post('/api/v1.0/reactions', validationMiddleware(ReactionRequestTo, reactionCreateError), reactionController.create);
        app.get('/api/v1.0/reactions', reactionController.findAll.bind(reactionController));
        app.get('/api/v1.0/reactions/:id', reactionController.findById);
        app.put('/api/v1.0/reactions', validationMiddleware(ReactionRequestTo, reactionModifierError), reactionController.update);
        app.delete('/api/v1.0/reactions/:id', reactionController.delete);

        // Роуты для Issue
        app.post('/api/v1.0/issues/:id/reactions', issueController.addReaction);
        app.post('/api/v1.0/issues', validationMiddleware(IssueRequestTo, issueCreateError), issueController.create);
        app.get('/api/v1.0/issues', issueController.findAll);
        app.get('/api/v1.0/issues/:id', issueController.findById);
        app.put('/api/v1.0/issues', validationMiddleware(IssueRequestTo, issueModifierError), issueController.update);
        app.delete('/api/v1.0/issues/:id', issueController.delete);

        // Централизованная обработка ошибок
        app.use(errorHandler);

        // Запускаем сервер
        const PORT = 24110;
        app.listen(PORT, () => {
            console.log(`Server is running on http://localhost:${PORT}`);
        });
    } catch (error) {
        console.error('Failed to initialize application:', error);
        process.exit(1); // Завершаем процесс при ошибке
    }
}

// Запускаем приложение
initializeApp();

export default app;