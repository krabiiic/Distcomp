export class ErrorMessages {
  static LabelNotFoundMessage(id: number): string {
    return `Label with id ${id} was not found.`;
  }

  static ArticleNotFoundMessage(id: number): string {
    return `Article with id ${id} was not found.`; 
  }

  static NoteNotFoundMessage(id: number): string {
    return `Note with id ${id} was not found.`; 
  }

  static LabelAlreadyExists(name: string): string {
    return `Label with name '${name}' already exists.`;
  }

  static ArticleAlreadyExists(title: string): string {
    return `Article with title '${title}' already exists.`;  
  }

  static NoteAlreadyExists(content: string): string {
    return `Note with content '${content}' already exists.`;  
  }
}
