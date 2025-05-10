import { MarkerService } from '../services/marker.service';
export class MarkerController {
    constructor() {
        this.markerService = new MarkerService();
        this.create = async (req, res) => {
            try {
                const markerDto = req.body;
                const marker = await this.markerService.create(markerDto);
                res.status(201).json(marker);
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
                const markers = await this.markerService.findAll(page, limit, sortBy, order);
                res.status(200).json(markers);
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.findById = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const marker = await this.markerService.findById(id);
                if (marker) {
                    res.status(200).json(marker);
                }
                else {
                    res.status(404).json({ message: 'Marker not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
        this.update = async (req, res) => {
            try {
                const id = parseInt(req.body.id, 10);
                const markerDto = req.body;
                const marker = await this.markerService.update(id, markerDto);
                if (marker) {
                    res.status(200).json(marker);
                }
                else {
                    res.status(404).json({ message: 'Marker not found' });
                }
            }
            catch (error) {
                res.status(400).json({ message: error.message });
            }
        };
        this.delete = async (req, res) => {
            try {
                const id = parseInt(req.params.id, 10);
                const isDeleted = await this.markerService.delete(id);
                if (isDeleted) {
                    res.status(204).send();
                }
                else {
                    res.status(404).json({ message: 'Marker not found' });
                }
            }
            catch (error) {
                res.status(500).json({ message: 'Internal server error' });
            }
        };
    }
}
