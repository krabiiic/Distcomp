import { Article } from '../entities/article.entity';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
export class ArticleService {
    constructor() {
        this.articleRepository = getRepository(Article);
    }
    async create(articleDto) {
        const errors = await validate(articleDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const article = this.articleRepository.create(articleDto);
        await this.articleRepository.save(article);
        return {
            id: article.id,
            title: article.title,
            content: article.content,
            userId: article.user.id,
            created: article.created,
            modified: article.modified,
        };
    }
    async findAll(page = 1, limit = 10, sortBy = 'id', order = 'ASC') {
        const [articles] = await this.articleRepository.findAndCount({
            skip: (page - 1) * limit,
            take: limit,
            order: { [sortBy]: order },
        });
        return articles.map(article => ({
            id: article.id,
            title: article.title,
            content: article.content,
            userId: article.user.id,
            created: article.created,
            modified: article.modified,
        }));
    }
    async findById(id) {
        const article = await this.articleRepository.findOne({ where: { id }, relations: ['user'] });
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
    }
    async update(id, articleDto) {
        const errors = await validate(articleDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const article = await this.articleRepository.findOne({ where: { id } });
        if (article) {
            Object.assign(article, articleDto);
            await this.articleRepository.save(article);
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
    }
    async delete(id) {
        const result = await this.articleRepository.delete(id);
        return result.affected > 0;
    }
}
