import { UserService } from '../services/user.sevice';
export class UserController {
    constructor() {
        this.userService = new UserService();
        this.create = async (req, res) => {
            try {
                const userDto = req.body;
                const user = await this.userService.create(userDto);
                res.status(201).json(user);
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.findAll = async (req, res) => {
            try {
                const users = await this.userService.findAll();
                res.status(200).json(users);
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.findById = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const user = await this.userService.findById(id);
                if (user) {
                    res.status(200).json(user);
                }
                else {
                    res.status(404).json({ message: 'User not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.update = async (req, res) => {
            try {
                const id = parseInt(req.body.id, 10);
                const userDto = req.body;
                const user = await this.userService.update(id, userDto);
                if (user) {
                    res.status(200).json(user);
                }
                else {
                    res.status(404).json({ message: 'User not found' });
                }
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.delete = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const isDeleted = await this.userService.delete(id);
                if (isDeleted) {
                    res.status(204).send();
                }
                else {
                    res.status(404).json({ message: 'User not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
    }
}
