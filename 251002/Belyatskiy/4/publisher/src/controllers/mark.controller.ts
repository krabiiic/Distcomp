import { Request, Response } from 'express';
import { MarkService } from '../services/mark.service.js';
import { MarkRequestTo } from '../dtos/mark.dto.js';

export class MarkController {
    private markService = new MarkService();

    create = async (req: Request, res: Response) => {
        try {
            const markDto: MarkRequestTo = req.body;
            const mark = await this.markService.create(markDto);
            res.status(201).json(mark);
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    findAll = async (req: Request, res: Response) => {
        try {
            const page = parseInt(req.query.page as string, 10) || 1;
            const limit = parseInt(req.query.limit as string, 10) || 10;
            const sortBy = req.query.sortBy as string || 'id';
            const order = req.query.order as 'ASC' | 'DESC' || 'ASC';

            const marks = await this.markService.findAll(page, limit, sortBy, order);
            res.status(200).json(marks);
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const mark = await this.markService.findById(id);
            if (mark) {
                res.status(200).json(mark);
            } else {
                res.status(404).json({ message: 'Mark not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    update = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.body.id, 10);
            const markDto: MarkRequestTo = req.body;
            const mark = await this.markService.update(id, markDto);
            if (mark) {
                res.status(200).json(mark);
            } else {
                res.status(404).json({ message: 'Mark not found' });
            }
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    delete = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const isDeleted = await this.markService.delete(id);
            if (isDeleted) {
                res.status(204).send();
            } else {
                res.status(404).json({ message: 'Mark not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}