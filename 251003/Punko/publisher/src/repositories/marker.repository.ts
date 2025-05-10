import { EntityRepository, Repository } from 'typeorm';
import { Marker } from '../entities/marker.entity.js';

@EntityRepository(Marker)
export class MarkerRepository extends Repository<Marker> {}