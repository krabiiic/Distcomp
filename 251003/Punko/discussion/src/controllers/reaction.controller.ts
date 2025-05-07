import { ReactionService } from '../services/reaction.service.js';
import { ReactionRequestTo } from '../dtos/reaction.dto.js';

export class ReactionController {
    private reactionService = new ReactionService();

    async create(req: any, res: any, next: any) {
        try {
            const reactionDto: ReactionRequestTo = req.body;
            const result = await this.reactionService.create(reactionDto);
            res.status(201).json(result);
        } catch (error) {
            next(error);
        }
    }

    async createFromKafka(dto: ReactionRequestTo) {
        await this.reactionService.create(dto);
    }

    async findAll(req: any, res: any, next: any) {
        try {
            const page = parseInt(req.query.page as string, 10) || 1;
            const limit = parseInt(req.query.limit as string, 10) || 10;
            const sortBy = req.query.sortBy as string || 'id';
            const order = req.query.order as 'ASC' | 'DESC' || 'ASC';
            const result = await this.reactionService.findAll(page, limit, sortBy, order);
            res.status(200).json(result || []);
        } catch (error) {
            next(error);
        }
    }

    async findById(req: any, res: any, next: any) {
        try {
            const id = parseInt(req.params.id);
            if (isNaN(id)) {
                return res.status(400).send('Invalid ID');
            }
            const result = await this.reactionService.findById(id);
            if (result) {
                res.status(200).json(result);
            } else {
                res.status(404).send({message:'Reaction not found'});
            }
        } catch (error) {
            next(error);
        }
    }

    async update(req: any, res: any, next: any) {
        try {
            const reactionDto: ReactionRequestTo = req.body;
            console.log('Updating reaction with DTO:', reactionDto);
            const result = await this.reactionService.update(reactionDto);
            console.log('Updated reaction:', result);
            if (result) {
                res.status(200).json(result);
            } else {
                console.log('Reaction not found');
                res.status(404).send('Reaction not found');
            }
        } catch (error) {
            next(error);
        }
    }

    async delete(req: any, res: any, next: any) {
        try {
            const id = parseInt(req.params.id);
            if (isNaN(id)) {
                return res.status(400).send('Invalid ID');
            }
            console.log("Удаление: "+id)
            const success = await this.reactionService.delete(id);

            res.status(success ? 204 : 404).send();
        } catch (error) {
            next(error);
        }
    }
}