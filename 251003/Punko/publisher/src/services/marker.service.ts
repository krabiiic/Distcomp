import { MarkerRequestTo, MarkerResponseTo } from '../dtos/marker.dto.js';
import { Marker } from '../entities/marker.entity.js';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
import {cacheService} from "./cache.service";

const MARKER_CACHE_PREFIX = 'marker';
const MARKER_FIND_BY_ID_PREFIX = `${MARKER_CACHE_PREFIX}:findById`;
const MARKER_FIND_ALL_PREFIX = `${MARKER_CACHE_PREFIX}:findAll`;

export class MarkerService {
    private markerRepository = getRepository(Marker);

    async create(markerDto: MarkerRequestTo): Promise<MarkerResponseTo> {
        const errors = await validate(markerDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        await cacheService.invalidatePattern(`${MARKER_FIND_ALL_PREFIX}*`);
        const marker = this.markerRepository.create(markerDto);
        await this.markerRepository.save(marker);
        return {
            id: marker.id,
            name: marker.name,
        };
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<MarkerResponseTo[] | null | undefined> {
        const cacheKey = MARKER_FIND_ALL_PREFIX;
        return await cacheService.getOrSet<MarkerResponseTo[]>(cacheKey, async () => {
            const [markers] = await this.markerRepository.findAndCount({
                skip: (page - 1) * limit,
                take: limit,
                order: {[sortBy]: order},
            });
            return markers.map(marker => ({
                id: marker.id,
                name: marker.name,
            }));
        })
    }

    async findById(id: number): Promise<MarkerResponseTo | undefined |null> {
        const cacheKey = `${MARKER_FIND_BY_ID_PREFIX}:${id}`;
        return await cacheService.getOrSet<MarkerResponseTo>(cacheKey, async () => {
            const marker = await this.markerRepository.findOne({where: {id}});
            if (marker) {
                return {
                    id: marker.id,
                    name: marker.name,
                };
            }
            return undefined;
        })
    }

    async update(id: number, markerDto: MarkerRequestTo): Promise<MarkerResponseTo | undefined> {
        const errors = await validate(markerDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }

        const marker = await this.markerRepository.findOne({ where: { id } });
        if (marker) {
            Object.assign(marker, markerDto);
            await this.markerRepository.save(marker);
            await cacheService.invalidate(`${MARKER_FIND_BY_ID_PREFIX}:${id}`);
            await cacheService.invalidatePattern(`${MARKER_FIND_ALL_PREFIX}*`);
            return {
                id: marker.id,
                name: marker.name,
            };
        }
        return undefined;
    }

    async delete(id: number): Promise<boolean> {
        const result = await this.markerRepository.delete(id);
        await cacheService.invalidate(`${MARKER_FIND_BY_ID_PREFIX}:${id}`);
        await cacheService.invalidatePattern(`${MARKER_FIND_ALL_PREFIX}*`);
        return result.affected !== null && result.affected !== undefined && result.affected > 0;
    }
}