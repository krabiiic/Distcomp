import { IssueRequestTo, IssueResponseTo } from '../dtos/issue.dto.js';
import { Issue } from '../entities/issue.entity.js';
import { validate } from 'class-validator';
import { getRepository } from 'typeorm';
import {Author} from "../entities/author.entity";
import {Marker} from "../entities/marker.entity";
import {ReactionRequestTo} from "../dtos/reaction.dto";
import axios from "axios";
import {producer} from "../kafka.js";
import { cacheService } from './cache.service.js'; // Импортируем сервис кэширования

const ISSUE_CACHE_PREFIX = 'issue';
const ISSUE_FIND_BY_ID_PREFIX = `${ISSUE_CACHE_PREFIX}:findById`;
const ISSUE_FIND_ALL_PREFIX = `${ISSUE_CACHE_PREFIX}:findAll`;

export class IssueService {
    private issueRepository = getRepository(Issue);
    private markerRepository = getRepository(Marker);
    private discussionApi = 'http://localhost:24130/api/v1.0/reactions';

    async create(issueDto: IssueRequestTo): Promise<IssueResponseTo> {
        const errors = await validate(issueDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }

        // Проверяем, существует ли Issue с таким title
        const existingIssue = await this.issueRepository.findOne({ where: { title: issueDto.title } });
        if (existingIssue) {
            const error = new Error('Issue with this title already exists'); // Исправлено сообщение
            (error as any).status = 403;
            throw error;
        }

        // Проверяем, существует ли автор
        const author = await getRepository(Author).findOne({ where: { id: issueDto.authorId } });
        if (!author) {
            const error = new Error('Author with this ID does not exist');
            (error as any).status = 404; // 404 — более подходящий статус для "не найдено"
            throw error;
        }
        // Обрабатываем маркеры
        let markers: Marker[] = [];
        if (issueDto.markers && issueDto.markers.length > 0) {
            markers = await Promise.all(
                issueDto.markers.map(async (markerName) => {
                    // Проверяем, существует ли маркер с таким именем
                    let marker = await this.markerRepository.findOne({ where: { name: markerName } });
                    if (!marker) {
                        // Если маркер не существует, создаем новый
                        marker = this.markerRepository.create({ name: markerName });
                        await this.markerRepository.save(marker);
                    }
                    return marker;
                })
            );
        }
        const time = Date.now();
        // Создаем объект Issue с правильной связью author
        const issueWithTime = {
            ...issueDto,
            created: time,
            modified: time,
            author,
            markers
        };
        const issue = this.issueRepository.create(issueWithTime);
        await this.issueRepository.save(issue);

        // Загружаем issue с author, чтобы получить author.id
        const savedIssue = await this.issueRepository.findOne({
            where: { id: issue.id },
            relations: ['author','markers']
        });

        if (!savedIssue) {
            throw new Error('Failed to retrieve saved issue');
        }
        await cacheService.invalidatePattern(`${ISSUE_FIND_ALL_PREFIX}*`);
        return {
            id: savedIssue.id,
            title: savedIssue.title,
            content: savedIssue.content,
            authorId: savedIssue.author.id, // Теперь author загружен и id доступен
            created: savedIssue.created,
            modified: savedIssue.modified,
        };

    }

    async addReaction(issueId: number, reactionDto: ReactionRequestTo) {
        reactionDto.issueId = issueId;
        await cacheService.invalidatePattern(`${ISSUE_FIND_ALL_PREFIX}*`);
        await producer.send({
            topic: 'reaction-topic',
            messages: [{ value: JSON.stringify(reactionDto) }]
        });
    }

    async findAll(page: number = 1, limit: number = 10, sortBy: string = 'id', order: 'ASC' | 'DESC' = 'ASC'): Promise<IssueResponseTo[] | null | undefined> {
        const cacheKey = ISSUE_FIND_ALL_PREFIX;
        return await cacheService.getOrSet<IssueResponseTo[]>(cacheKey, async () => {
            const [issues] = await this.issueRepository.findAndCount({
                skip: (page - 1) * limit,
                take: limit,
                order: {[sortBy]: order},
                relations: ['author']
            });
            return issues.map(issue => ({
                id: issue.id,
                title: issue.title,
                content: issue.content,
                authorId: issue.author?.id,
                created: issue.created,
                modified: issue.modified,
            }));
        });
    }

    async findById(id: number): Promise<IssueResponseTo | undefined | null> {
        const cacheKey = `${ISSUE_FIND_BY_ID_PREFIX}:${id}`;
        return await cacheService.getOrSet<IssueResponseTo>(cacheKey, async () => {
            const issue = await this.issueRepository.findOne({where: {id}, relations: ['author']});
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
        });
    }
    async update(issueDto: IssueRequestTo): Promise<IssueResponseTo | undefined> {
        const errors = await validate(issueDto);
        if (errors.length > 0) {
            throw new Error('Validation failed');
        }

        if (!issueDto.id) {
            const error = new Error('Issue ID is required for update');
            (error as any).status = 400;
            throw error;
        }

        const issue = await this.issueRepository.findOne({ where: { id: issueDto.id }, relations: ['author'] });
        if (!issue) {
            const error = new Error('Issue not found');
            (error as any).status = 404;
            throw error;
        }

        const author = await getRepository(Author).findOne({ where: { id: issueDto.authorId } });
        if (!author) {
            const error = new Error('Author not found');
            (error as any).status = 404;
            throw error;
        }

        // Обновляем только нужные поля
        issue.title = issueDto.title;
        issue.content = issueDto.content;
        issue.author = author;
        issue.modified = Date.now();

        await this.issueRepository.save(issue);

        // Загружаем обновленную запись для точности
        const updatedIssue = await this.issueRepository.findOne({
            where: { id: issue.id },
            relations: ['author']
        });

        if (!updatedIssue) {
            throw new Error('Failed to retrieve updated issue');
        }
        await cacheService.invalidate(`${ISSUE_FIND_BY_ID_PREFIX}:${updatedIssue.id}`);
        await cacheService.invalidatePattern(`${ISSUE_FIND_ALL_PREFIX}*`);
        return {
            id: updatedIssue.id,
            title: updatedIssue.title,
            content: updatedIssue.content,
            authorId: updatedIssue.author.id,
            created: updatedIssue.created,
            modified: updatedIssue.modified,
        };
    }

    async delete(id: number): Promise<boolean> {
        const issue = await this.issueRepository.findOne({
            where: { id },
            relations: ['markers'],
        });

        if (!issue) {
            return false;
        }

        const deleteResult = await this.issueRepository.delete(id);
        if (deleteResult.affected === null || deleteResult.affected === undefined || deleteResult.affected === 0) {
            return false;
        }

        if (issue.markers && issue.markers.length > 0) {
            for (const marker of issue.markers) {
                const relatedIssues = await this.issueRepository
                    .createQueryBuilder('issue')
                    .innerJoin('issue.markers', 'marker')
                    .where('marker.id = :markerId', { markerId: marker.id })
                    .getCount();

                if (relatedIssues === 0) {
                    await this.markerRepository.delete(marker.id);
                }
            }
        }
        await cacheService.invalidate(`${ISSUE_FIND_BY_ID_PREFIX}:${id}`);
        await cacheService.invalidatePattern(`${ISSUE_FIND_ALL_PREFIX}*`);
        return true;
    }
}