import { ReactionService } from '../services/reaction.service';
export class ReactionController {
    constructor() {
        this.reactionService = new ReactionService();
        this.create = async (req, res) => {
            try {
                const reactionDto = req.body;
                const reaction = await this.reactionService.create(reactionDto);
                res.status(201).json(reaction);
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
                const reactions = await this.reactionService.findAll(page, limit, sortBy, order);
                res.status(200).json(reactions);
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.findById = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const reaction = await this.reactionService.findById(id);
                if (reaction) {
                    res.status(200).json(reaction);
                }
                else {
                    res.status(404).json({ message: 'Reaction not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.update = async (req, res) => {
            try {
                const id = parseInt(req.body.id, 10);
                const reactionDto = req.body;
                const reaction = await this.reactionService.update(id, reactionDto);
                if (reaction) {
                    res.status(200).json(reaction);
                }
                else {
                    res.status(404).json({ message: 'Reaction not found' });
                }
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.delete = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const isDeleted = await this.reactionService.delete(id);
                if (isDeleted) {
                    res.status(204).send();
                }
                else {
                    res.status(404).json({ message: 'Reaction not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
    }
}
