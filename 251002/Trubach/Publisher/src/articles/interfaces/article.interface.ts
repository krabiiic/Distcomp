import { Article } from '../article.entity';

export interface IArticleRepository {
  create(article: Article): Promise<Article>;
  findAll(): Promise<Article[]>;
  findOne(id: number): Promise<Article | null>;
  update(id: number, article: Article): Promise<Article | null>;
  delete(id: number): Promise<boolean>;
}
