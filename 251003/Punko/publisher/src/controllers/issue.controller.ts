import { Request, Response } from 'express';
import { IssueService } from '../services/issue.service.js';
import { IssueRequestTo } from '../dtos/issue.dto.js';
import {ReactionRequestTo} from "../dtos/reaction.dto";

export class IssueController {
    private issueService = new IssueService();

    create = async (req: Request, res: Response) => {
        try {
            const issueDto: IssueRequestTo = req.body;
            const issue = await this.issueService.create(issueDto);
            res.status(201).json(issue);
        } catch (error) {
            res.status(403).json({ message: error });
        }
    };
    async addReaction(req: any, res: any, next: any) {
        try {
            const issueId = parseInt(req.params.id);
            const reactionDto: ReactionRequestTo = req.body;
            const result = await this.issueService.addReaction(issueId, reactionDto);
            res.status(201).json(result);
        } catch (error) {
            next(error);
        }
    }
    findAll = async (req: Request, res: Response) => {
        try {
            const page = parseInt(req.query.page as string, 10) || 1;
            const limit = parseInt(req.query.limit as string, 10) || 10;
            const sortBy = req.query.sortBy as string || 'id';
            const order = req.query.order as 'ASC' | 'DESC' || 'ASC';

            const issues = await this.issueService.findAll(page, limit, sortBy, order);
            res.status(200).json(issues);
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const issue = await this.issueService.findById(id);
            if (issue) {
                res.status(200).json(issue);
            } else {
                res.status(404).json({ message: 'Issue not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    update = async (req: Request, res: Response) => {
        try {
            const issueDto: IssueRequestTo = req.body;
            const issue = await this.issueService.update(issueDto);
            if (issue) {
                res.status(200).json(issue);
            } else {
                res.status(404).json({ message: 'Issue not found' });
            }
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    delete = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const isDeleted = await this.issueService.delete(id);
            if (isDeleted) {
                res.status(204).send();
            } else {
                res.status(404).json({ message: 'Issue not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}