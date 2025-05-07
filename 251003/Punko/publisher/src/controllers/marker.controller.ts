import { Request, Response } from 'express';
import { MarkerService } from '../services/marker.service.js';
import { MarkerRequestTo } from '../dtos/marker.dto.js';

export class MarkerController {
    private markerService = new MarkerService();

    create = async (req: Request, res: Response) => {
        try {
            const markerDto: MarkerRequestTo = req.body;
            const marker = await this.markerService.create(markerDto);
            res.status(201).json(marker);
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

            const markers = await this.markerService.findAll(page, limit, sortBy, order);
            res.status(200).json(markers);
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const marker = await this.markerService.findById(id);
            if (marker) {
                res.status(200).json(marker);
            } else {
                res.status(404).json({ message: 'Marker not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    update = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.body.id, 10);
            const markerDto: MarkerRequestTo = req.body;
            const marker = await this.markerService.update(id, markerDto);
            if (marker) {
                res.status(200).json(marker);
            } else {
                res.status(404).json({ message: 'Marker not found' });
            }
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    delete = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const isDeleted = await this.markerService.delete(id);
            if (isDeleted) {
                res.status(204).send();
            } else {
                res.status(404).json({ message: 'Marker not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}