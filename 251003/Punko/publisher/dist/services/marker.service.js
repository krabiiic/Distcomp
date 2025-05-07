import { Marker } from '../entities/marker.entity';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
export class MarkerService {
    constructor() {
        this.markerRepository = getRepository(Marker);
    }
    async create(markerDto) {
        const errors = await validate(markerDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const marker = this.markerRepository.create(markerDto);
        await this.markerRepository.save(marker);
        return {
            id: marker.id,
            name: marker.name,
        };
    }
    async findAll(page = 1, limit = 10, sortBy = 'id', order = 'ASC') {
        const [markers] = await this.markerRepository.findAndCount({
            skip: (page - 1) * limit,
            take: limit,
            order: { [sortBy]: order },
        });
        return markers.map(marker => ({
            id: marker.id,
            name: marker.name,
        }));
    }
    async findById(id) {
        const marker = await this.markerRepository.findOne({ where: { id } });
        if (marker) {
            return {
                id: marker.id,
                name: marker.name,
            };
        }
        return undefined;
    }
    async update(id, markerDto) {
        const errors = await validate(markerDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const marker = await this.markerRepository.findOne({ where: { id } });
        if (marker) {
            Object.assign(marker, markerDto);
            await this.markerRepository.save(marker);
            return {
                id: marker.id,
                name: marker.name,
            };
        }
        return undefined;
    }
    async delete(id) {
        const result = await this.markerRepository.delete(id);
        return result.affected > 0;
    }
}
