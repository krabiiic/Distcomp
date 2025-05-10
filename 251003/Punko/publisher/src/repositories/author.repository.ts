import { EntityRepository, Repository } from 'typeorm';
import { Author } from '../entities/author.entity.js';

@EntityRepository(Author)
export class AuthorRepository extends Repository<Author> {}