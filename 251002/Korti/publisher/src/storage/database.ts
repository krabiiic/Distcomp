import { Article } from 'src/entities/Article';
import { Editor } from 'src/entities/Editor';
import { Sticker } from 'src/entities/Sticker';
import { Note } from 'src/entities/Note';
import { ConflictException, NotFoundException } from '@nestjs/common';

interface HasId {
  id: number;
}

export enum CollectionType {
  EDITORS = 'editors',
  ARTICLES = 'articles',
  STICKERS = 'stickers',
  NOTES = 'notes',
}

export class StorageService {
  private static editors: Editor[] = [];
  private static articles: Article[] = [];
  private static stickers: Sticker[] = [];
  private static notes: Note[] = [];

  private static collectionMap = {
    [CollectionType.EDITORS]: this.editors,
    [CollectionType.ARTICLES]: this.articles,
    [CollectionType.STICKERS]: this.stickers,
    [CollectionType.NOTES]: this.notes,
  };

  private static async getCollection<T extends HasId>(
    type: CollectionType,
  ): Promise<T[]> {
    const collection = this.collectionMap[type];

    if (!collection) {
      throw new Error(`Collection type ${type} not found`);
    }

    return collection as unknown as T[];
  }

  private static async store<T extends HasId>(
    type: CollectionType,
    item: T,
  ): Promise<T> {
    const collection = await this.getCollection<T>(type);
    let exists: boolean;
    let authorCheck: boolean;
    switch (type) {
      case CollectionType.EDITORS:
        exists = (collection as unknown as Editor[]).some(
          (el) => el.login === (item as unknown as Editor).login,
        );
        break;
      case CollectionType.ARTICLES:
        authorCheck = this.editors.some(
          (el) => el.id === (item as unknown as Article).editorId,
        );
        if (!authorCheck) throw new NotFoundException();
        exists = (collection as unknown as Article[]).some(
          (el) => el.title === (item as unknown as Article).title,
        );
        break;
      case CollectionType.STICKERS:
        exists = (collection as unknown as Sticker[]).some(
          (el) => el.name === (item as unknown as Sticker).name,
        );
        break;
      case CollectionType.NOTES:
        authorCheck = this.articles.some(
          (el) => el.id == (item as unknown as Note).articleId,
        );
        if (!authorCheck) throw new NotFoundException();
        exists = true;
        break;
      default:
        exists = collection.some((el) => el.id === item.id);
    }
    if (!exists) {
      collection.push({ ...item });
      return item;
    } else {
      throw new ConflictException();
    }
  }

  private static async findById<T extends HasId>(
    type: CollectionType,
    id: number,
    errorMessage = 'Item not found',
  ): Promise<T> {
    const collection = await this.getCollection<T>(type);
    const item = collection.find((el) => el.id === id);

    if (!item) {
      throw new Error(errorMessage);
    }

    return item;
  }

  static async update<T extends HasId>(
    type: CollectionType,
    item: T,
  ): Promise<T> {
    const collection = await this.getCollection<T>(type);
    try {
      await this.findById(type, item.id);
    } catch {
      throw new ConflictException();
    }

    const index = collection.findIndex((el) => el.id === item.id);
    collection[index] = item;

    return item;
  }

  static async add<T extends HasId>(type: CollectionType, item: T): Promise<T> {
    return await this.store(type, item);
  }

  static async getAll<T extends HasId>(
    type: CollectionType,
  ): Promise<ReadonlyArray<T>> {
    const collection = await this.getCollection<T>(type);
    return [...collection];
  }

  static async getById<T extends HasId>(
    type: CollectionType,
    id: number,
  ): Promise<T> {
    return await this.findById(type, id);
  }

  static async remove<T extends HasId>(
    type: CollectionType,
    id: number,
  ): Promise<Readonly<T>> {
    const collection = await this.getCollection(type);
    const index = collection.findIndex((item) => item.id === id);
    if (index !== -1) {
      const item = collection[index] as T;
      collection.splice(index, 1);
      return item;
    } else {
      throw new ConflictException();
    }
  }

  static async generateId(type: CollectionType): Promise<number> {
    const collection = await this.getCollection(type);
    const existingIds = new Set(collection.map((item) => item.id));
    let newId = 1;
    while (existingIds.has(newId)) {
      newId++;
    }
    return newId;
  }

  static async getEditorByArticleId(id: number): Promise<Editor> {
    const article = this.articles.find((el) => el.id == id);
    if (article) {
      const editor = await this.getById<Editor>(
        CollectionType.EDITORS,
        article.editorId,
      );
      return editor;
    } else {
      throw new ConflictException();
    }
  }
}
