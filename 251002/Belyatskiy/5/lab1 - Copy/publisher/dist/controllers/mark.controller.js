import { MarkService } from '../services/mark.service';
export class MarkController {
    constructor() {
        this.markService = new MarkService();
        this.create = async (req, res) => {
            try {
                const markDto = req.body;
                const mark = await this.markService.create(markDto);
                res.status(201).json(mark);
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
                const marks = await this.markService.findAll(page, limit, sortBy, order);
                res.status(200).json(marks);
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.findById = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const mark = await this.markService.findById(id);
                if (mark) {
                    res.status(200).json(mark);
                }
                else {
                    res.status(404).json({ message: 'Mark not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.update = async (req, res) => {
            try {
                const id = parseInt(req.body.id, 10);
                const markDto = req.body;
                const mark = await this.markService.update(id, markDto);
                if (mark) {
                    res.status(200).json(mark);
                }
                else {
                    res.status(404).json({ message: 'Mark not found' });
                }
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.delete = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const isDeleted = await this.markService.delete(id);
                if (isDeleted) {
                    res.status(204).send();
                }
                else {
                    res.status(404).json({ message: 'Mark not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
    }
}
