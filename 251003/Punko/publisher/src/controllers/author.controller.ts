import { Request, Response } from 'express';
import { AuthorService } from '../services/author.sevice.js';
import { AuthorRequestTo } from '../dtos/author.dto.js';

export class AuthorController {
    private authorService = new AuthorService();

    create = async (req: Request, res: Response) => {
        try {
            const authorDto: AuthorRequestTo = req.body;
            const author = await this.authorService.create(authorDto);
            res.status(201).json(author);
        } catch (error) {
            res.status(403).json({ message: error });
        }
    };

    findAll = async (req: Request, res: Response) => {
        try {
            const authors = await this.authorService.findAll();
            res.status(200).json(authors);
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const author = await this.authorService.findById(id);
            if (author) {
                res.status(200).json(author);
            } else {
                res.status(404).json({ message: 'Author not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    update = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.body.id, 10);
            const authorDto: AuthorRequestTo = req.body;
            const author = await this.authorService.update(id, authorDto);
            if (author) {
                res.status(200).json(author);
            } else {
                res.status(404).json({ message: 'Author not found' });
            }
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    delete = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const isDeleted = await this.authorService.delete(id);
            if (isDeleted) {
                res.status(204).send();
            } else {
                res.status(404).json({ message: 'Author not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}