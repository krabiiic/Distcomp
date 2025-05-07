import { AuthorService } from '../services/author.sevice';
export class AuthorController {
    constructor() {
        this.authorService = new AuthorService();
        this.create = async (req, res) => {
            try {
                const authorDto = req.body;
                const author = await this.authorService.create(authorDto);
                res.status(201).json(author);
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.findAll = async (req, res) => {
            try {
                const authors = await this.authorService.findAll();
                res.status(200).json(authors);
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.findById = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const author = await this.authorService.findById(id);
                if (author) {
                    res.status(200).json(author);
                }
                else {
                    res.status(404).json({ message: 'Author not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.update = async (req, res) => {
            try {
                const id = parseInt(req.body.id, 10);
                const authorDto = req.body;
                const author = await this.authorService.update(id, authorDto);
                if (author) {
                    res.status(200).json(author);
                }
                else {
                    res.status(404).json({ message: 'Author not found' });
                }
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.delete = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const isDeleted = await this.authorService.delete(id);
                if (isDeleted) {
                    res.status(204).send();
                }
                else {
                    res.status(404).json({ message: 'Author not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
    }
}
