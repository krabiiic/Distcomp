import { ArticleRequestTo, ArticleResponseTo } from '../dtos/article.dto.js';
import { Article } from '../entities/article.entity.js';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
import {User} from "../entities/user.entity";
import {Mark} from "../entities/mark.entity";
import {MessageRequestTo} from "../dtos/message.dto";
import axios from "axios";
import {producer} from "../kafka.js";
import { cacheService } from './cache.service.js'; // Импортируем сервис кэширования

const ISSUE_CACHE_PREFIX = 'article';
const ISSUE_FIND_BY_ID_PREFIX = `${ISSUE_CACHE_PREFIX}:findById`;
const ISSUE_FIND_ALL_PREFIX = `${ISSUE_CACHE_PREFIX}:findAll`;

export class ArticleService {
    private articleRepository = getRepository(Article);
    private markRepository = getRepository(Mark);
    private discussionApi = 'http://localhost:24130/api/v1.0/messages';

    async create(articleDto: ArticleRequestTo): Promise<ArticleResponseTo> {
        const errors = await validate(articleDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }

        // Проверяем, существует ли Article с таким title
        const existingArticle = await this.articleRepository.findOne({ where: { title: articleDto.title } });
        if (existingArticle) {
            const error = new Error('Article with this title already exists'); // Исправлено сообщение
            (error as any).status = 403;
            throw error;
        }

        // Проверяем, существует ли автор
        const user = await getRepository(User).findOne({ where: { id: articleDto.userId } });
        if (!user) {
            const error = new Error('User with this ID does not exist');
            (error as any).status = 404; // 404 — более подходящий статус для "не найдено"
            throw error;
        }
        // Обрабатываем маркеры
        let marks: Mark[] = [];
        if (articleDto.marks && articleDto.marks.length > 0) {
            marks = await Promise.all(
                articleDto.marks.map(async (markName) => {
                    // Проверяем, существует ли маркер с таким именем
                    let mark = await this.markRepository.findOne({ where: { name: markName } });
                    if (!mark) {
                        // Если маркер не существует, создаем новый
                        mark = this.markRepository.create({ name: markName });
                        await this.markRepository.save(mark);
                    }
                    return mark;
                })
            );
        }
        const time = Date.now();
        // Создаем объект Article с правильной связью user
        const articleWithTime = {
            ...articleDto,
            created: time,
            modified: time,
            user,
            marks
        };
        const article = this.articleRepository.create(articleWithTime);
        await this.articleRepository.save(article);

        // Загружаем article с user, чтобы получить user.id
        const savedArticle = await this.articleRepository.findOne({
            where: { id: article.id },
            relations: ['user','marks']
        });

        if (!savedArticle) {
            throw new Error('Failed to retrieve saved article');
        }
        await cacheService.invalidatePattern(`${ISSUE_FIND_ALL_PREFIX}*`);
        return {
            id: savedArticle.id,
            title: savedArticle.title,
            content: savedArticle.content,
            userId: savedArticle.user.id, // Теперь user загружен и id доступен
            created: savedArticle.created,
            modified: savedArticle.modified,
        };

    }

    async addMessage(articleId: number, messageDto: MessageRequestTo) {
        messageDto.articleId = articleId;
        await cacheService.invalidatePattern(`${ISSUE_FIND_ALL_PREFIX}*`);
        await producer.send({
            topic: 'message-topic',
            messages: [{ value: JSON.stringify(messageDto) }]
        });
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<ArticleResponseTo[] | null | undefined> {
        const cacheKey = ISSUE_FIND_ALL_PREFIX;
        return await cacheService.getOrSet<ArticleResponseTo[]>(cacheKey, async () => {
            const [articles] = await this.articleRepository.findAndCount({
                skip: (page - 1) * limit,
                take: limit,
                order: {[sortBy]: order},
                relations: ['user']
            });
            return articles.map(article => ({
                id: article.id,
                title: article.title,
                content: article.content,
                userId: article.user?.id,
                created: article.created,
                modified: article.modified,
            }));
        });
    }

    async findById(id: number): Promise<ArticleResponseTo | undefined | null> {
        const cacheKey = `${ISSUE_FIND_BY_ID_PREFIX}:${id}`;
        return await cacheService.getOrSet<ArticleResponseTo>(cacheKey, async () => {
            const article = await this.articleRepository.findOne({where: {id}, relations: ['user']});
            if (article) {
                return {
                    id: article.id,
                    title: article.title,
                    content: article.content,
                    userId: article.user.id,
                    created: article.created,
                    modified: article.modified,
                };
            }
            return undefined;
        });
    }
    async update(articleDto: ArticleRequestTo): Promise<ArticleResponseTo | undefined> {
        const errors = await validate(articleDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }

        if (!articleDto.id) {
            const error = new Error('Article ID is required for update');
            (error as any).status = 400;
            throw error;
        }

        const article = await this.articleRepository.findOne({ where: { id: articleDto.id }, relations: ['user'] });
        if (!article) {
            const error = new Error('Article not found');
            (error as any).status = 404;
            throw error;
        }

        const user = await getRepository(User).findOne({ where: { id: articleDto.userId } });
        if (!user) {
            const error = new Error('User not found');
            (error as any).status = 404;
            throw error;
        }

        // Обновляем только нужные поля
        article.title = articleDto.title;
        article.content = articleDto.content;
        article.user = user;
        article.modified = Date.now();

        await this.articleRepository.save(article);

        // Загружаем обновленную запись для точности
        const updatedArticle = await this.articleRepository.findOne({
            where: { id: article.id },
            relations: ['user']
        });

        if (!updatedArticle) {
            throw new Error('Failed to retrieve updated article');
        }
        await cacheService.invalidate(`${ISSUE_FIND_BY_ID_PREFIX}:${updatedArticle.id}`);
        await cacheService.invalidatePattern(`${ISSUE_FIND_ALL_PREFIX}*`);
        return {
            id: updatedArticle.id,
            title: updatedArticle.title,
            content: updatedArticle.content,
            userId: updatedArticle.user.id,
            created: updatedArticle.created,
            modified: updatedArticle.modified,
        };
    }

    async delete(id: number): Promise<boolean> {
        const article = await this.articleRepository.findOne({
            where: { id },
            relations: ['marks'],
        });

        if (!article) {
            return false;
        }

        const deleteResult = await this.articleRepository.delete(id);
        if (deleteResult.affected === null || deleteResult.affected === undefined || deleteResult.affected === 0) {
            return false;
        }

        if (article.marks && article.marks.length > 0) {
            for (const mark of article.marks) {
                const relatedArticles = await this.articleRepository
                    .createQueryBuilder('article')
                    .innerJoin('article.marks', 'mark')
                    .where('mark.id = :markId', { markId: mark.id })
                    .getCount();

                if (relatedArticles === 0) {
                    await this.markRepository.delete(mark.id);
                }
            }
        }
        await cacheService.invalidate(`${ISSUE_FIND_BY_ID_PREFIX}:${id}`);
        await cacheService.invalidatePattern(`${ISSUE_FIND_ALL_PREFIX}*`);
        return true;
    }
}