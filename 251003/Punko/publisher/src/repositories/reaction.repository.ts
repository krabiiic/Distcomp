import { EntityRepository, Repository } from 'typeorm';
import { Reaction } from '../entities/reaction.entity.js';

@EntityRepository(Reaction)
export class ReactionRepository extends Repository<Reaction> {}