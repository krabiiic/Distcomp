import { Request, Response } from 'express';
import { ArticleService } from '../services/article.service.js';
import { ArticleRequestTo } from '../dtos/article.dto.js';

export class ArticleController {
    private articleService = new ArticleService();

    create = async (req: Request, res: Response) => {
        try {
            const articleDto: ArticleRequestTo = req.body;
            const article = await this.articleService.create(articleDto);
            res.status(201).json(article);
        } catch (error) {
            res.status(403).json({ message: error });
        }
    };

    findAll = async (req: Request, res: Response) => {
        try {
            const page = parseInt(req.query.page as string, 10) || 1;
            const limit = parseInt(req.query.limit as string, 10) || 10;
            const sortBy = req.query.sortBy as string || 'id';
            const order = req.query.order as 'ASC' | 'DESC' || 'ASC';

            const articles = await this.articleService.findAll(page, limit, sortBy, order);
            res.status(200).json(articles);
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const article = await this.articleService.findById(id);
            if (article) {
                res.status(200).json(article);
            } else {
                res.status(404).json({ message: 'Article not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    update = async (req: Request, res: Response) => {
        try {
            const articleDto: ArticleRequestTo = req.body;
            const article = await this.articleService.update(articleDto);
            if (article) {
                res.status(200).json(article);
            } else {
                res.status(404).json({ message: 'Article not found' });
            }
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    delete = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const isDeleted = await this.articleService.delete(id);
            if (isDeleted) {
                res.status(204).send();
            } else {
                res.status(404).json({ message: 'Article not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}