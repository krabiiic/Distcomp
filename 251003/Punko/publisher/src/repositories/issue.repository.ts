import { EntityRepository, Repository } from 'typeorm';
import { Issue } from '../entities/issue.entity.js';

@EntityRepository(Issue)
export class IssueRepository extends Repository<Issue> {}