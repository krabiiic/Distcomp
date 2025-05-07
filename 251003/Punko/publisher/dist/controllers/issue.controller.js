import { IssueService } from '../services/issue.service';
export class IssueController {
    constructor() {
        this.issueService = new IssueService();
        this.create = async (req, res) => {
            try {
                const issueDto = req.body;
                const issue = await this.issueService.create(issueDto);
                res.status(201).json(issue);
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
                const issues = await this.issueService.findAll(page, limit, sortBy, order);
                res.status(200).json(issues);
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.findById = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const issue = await this.issueService.findById(id);
                if (issue) {
                    res.status(200).json(issue);
                }
                else {
                    res.status(404).json({ message: 'Issue not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.update = async (req, res) => {
            try {
                const id = parseInt(req.body.id, 10);
                const issueDto = req.body;
                const issue = await this.issueService.update(id, issueDto);
                if (issue) {
                    res.status(200).json(issue);
                }
                else {
                    res.status(404).json({ message: 'Issue not found' });
                }
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.delete = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const isDeleted = await this.issueService.delete(id);
                if (isDeleted) {
                    res.status(204).send();
                }
                else {
                    res.status(404).json({ message: 'Issue not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
    }
}
