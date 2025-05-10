import express from 'express';
import 'reflect-metadata';
import { createConnection } from 'typeorm';
import { UserController } from './src/controllers/user.controller.js';
import { MarkController } from './src/controllers/mark.controller.js';
import { MessageController } from './src/controllers/message.controller.js';
import { ArticleController } from './src/controllers/article.controller.js';
import { UserRequestTo } from './src/dtos/user.dto.js';
import { validationMiddleware } from './middleware.js';
import { ArticleRequestTo } from './src/dtos/article.dto.js';
import { MarkRequestTo } from './src/dtos/mark.dto.js';
import { MessageRequestTo } from './src/dtos/message.dto.js';
import {
    userCreateError,
    userModifierError,
    articleCreateError,
    articleModifierError,
    markCreateError,
    markModifierError,
    messageCreateError,
    messageModifierError,
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
        const userController = new UserController();
        const markController = new MarkController();
        const messageController = new MessageController();
        const articleController = new ArticleController();

        // Роуты для User
        app.post('/api/v1.0/users', validationMiddleware(UserRequestTo, userCreateError), userController.create);
        app.get('/api/v1.0/users', userController.findAll);
        app.get('/api/v1.0/users/:id', userController.findById);
        app.put('/api/v1.0/users', validationMiddleware(UserRequestTo, userModifierError), userController.update);
        app.delete('/api/v1.0/users/:id', userController.delete);

        // Роуты для Mark
        app.post('/api/v1.0/marks', validationMiddleware(MarkRequestTo, markCreateError), markController.create);
        app.get('/api/v1.0/marks', markController.findAll);
        app.get('/api/v1.0/marks/:id', markController.findById);
        app.put('/api/v1.0/marks', validationMiddleware(MarkRequestTo, markModifierError), markController.update);
        app.delete('/api/v1.0/marks/:id', markController.delete);

        // Роуты для Message
        app.post('/api/v1.0/messages', validationMiddleware(MessageRequestTo, messageCreateError), messageController.create);
        app.get('/api/v1.0/messages', messageController.findAll.bind(messageController));
        app.get('/api/v1.0/messages/:id', messageController.findById);
        app.put('/api/v1.0/messages', validationMiddleware(MessageRequestTo, messageModifierError), messageController.update);
        app.delete('/api/v1.0/messages/:id', messageController.delete);

        // Роуты для Article
        app.post('/api/v1.0/articles/:id/messages', articleController.addMessage);
        app.post('/api/v1.0/articles', validationMiddleware(ArticleRequestTo, articleCreateError), articleController.create);
        app.get('/api/v1.0/articles', articleController.findAll);
        app.get('/api/v1.0/articles/:id', articleController.findById);
        app.put('/api/v1.0/articles', validationMiddleware(ArticleRequestTo, articleModifierError), articleController.update);
        app.delete('/api/v1.0/articles/:id', articleController.delete);

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