import { Request, Response } from 'express';
import { UserService } from '../services/user.sevice.js';
import { UserRequestTo } from '../dtos/user.dto.js';

export class UserController {
    private userService = new UserService();

    create = async (req: Request, res: Response) => {
        try {
            const userDto: UserRequestTo = req.body;
            const user = await this.userService.create(userDto);
            res.status(201).json(user);
        } catch (error) {
            res.status(403).json({ message: error });
        }
    };

    findAll = async (req: Request, res: Response) => {
        try {
            const users = await this.userService.findAll();
            res.status(200).json(users);
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    findById = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const user = await this.userService.findById(id);
            if (user) {
                res.status(200).json(user);
            } else {
                res.status(404).json({ message: 'User not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };

    update = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.body.id, 10);
            const userDto: UserRequestTo = req.body;
            const user = await this.userService.update(id, userDto);
            if (user) {
                res.status(200).json(user);
            } else {
                res.status(404).json({ message: 'User not found' });
            }
        } catch (error) {
            res.status(400).json({ message: error });
        }
    };

    delete = async (req: Request, res: Response) => {
        try {
            const id = parseInt(req.params.id, 10);
            const isDeleted = await this.userService.delete(id);
            if (isDeleted) {
                res.status(204).send();
            } else {
                res.status(404).json({ message: 'User not found' });
            }
        } catch (error) {
            res.status(500).json({ message: 'Internal server error' });
        }
    };
}