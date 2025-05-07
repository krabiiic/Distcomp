import { Issue } from '../entities/issue.entity';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
export class IssueService {
    constructor() {
        this.issueRepository = getRepository(Issue);
    }
    async create(issueDto) {
        const errors = await validate(issueDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const issue = this.issueRepository.create(issueDto);
        await this.issueRepository.save(issue);
        return {
            id: issue.id,
            title: issue.title,
            content: issue.content,
            authorId: issue.author.id,
            created: issue.created,
            modified: issue.modified,
        };
    }
    async findAll(page = 1, limit = 10, sortBy = 'id', order = 'ASC') {
        const [issues] = await this.issueRepository.findAndCount({
            skip: (page - 1) * limit,
            take: limit,
            order: { [sortBy]: order },
        });
        return issues.map(issue => ({
            id: issue.id,
            title: issue.title,
            content: issue.content,
            authorId: issue.author.id,
            created: issue.created,
            modified: issue.modified,
        }));
    }
    async findById(id) {
        const issue = await this.issueRepository.findOne({ where: { id }, relations: ['author'] });
        if (issue) {
            return {
                id: issue.id,
                title: issue.title,
                content: issue.content,
                authorId: issue.author.id,
                created: issue.created,
                modified: issue.modified,
            };
        }
        return undefined;
    }
    async update(id, issueDto) {
        const errors = await validate(issueDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }
        const issue = await this.issueRepository.findOne({ where: { id } });
        if (issue) {
            Object.assign(issue, issueDto);
            await this.issueRepository.save(issue);
            return {
                id: issue.id,
                title: issue.title,
                content: issue.content,
                authorId: issue.author.id,
                created: issue.created,
                modified: issue.modified,
            };
        }
        return undefined;
    }
    async delete(id) {
        const result = await this.issueRepository.delete(id);
        return result.affected > 0;
    }
}
