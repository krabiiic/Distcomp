import { ArticleService } from '../services/article.service';
export class ArticleController {
    constructor() {
        this.articleService = new ArticleService();
        this.create = async (req, res) => {
            try {
                const articleDto = req.body;
                const article = await this.articleService.create(articleDto);
                res.status(201).json(article);
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.findAll = async (req, res) => {
            try {
                const page = parseInt(req.query.page, 10) || 1;
                const limit = parseInt(req.query.limit, 10) || 10;
                const sortBy = req.query.sortBy || 'id';
                const order = req.query.order || 'ASC';
                const articles = await this.articleService.findAll(page, limit, sortBy, order);
                res.status(200).json(articles);
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.findById = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const article = await this.articleService.findById(id);
                if (article) {
                    res.status(200).json(article);
                }
                else {
                    res.status(404).json({ message: 'Article not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.update = async (req, res) => {
            try {
                const id = parseInt(req.body.id, 10);
                const articleDto = req.body;
                const article = await this.articleService.update(id, articleDto);
                if (article) {
                    res.status(200).json(article);
                }
                else {
                    res.status(404).json({ message: 'Article not found' });
                }
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.delete = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const isDeleted = await this.articleService.delete(id);
                if (isDeleted) {
                    res.status(204).send();
                }
                else {
                    res.status(404).json({ message: 'Article not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
    }
}
