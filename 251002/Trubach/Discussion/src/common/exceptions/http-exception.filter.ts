import {
  ExceptionFilter,
  Catch,
  ArgumentsHost,
  HttpException,
  HttpStatus,
} from '@nestjs/common';
import { ErrorCodes } from './error.codes';
import { ErrorMessages } from './error.messages';

@Catch(HttpException)
export class HttpExceptionFilter implements ExceptionFilter {
  catch(exception: HttpException, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse();
    const status = exception.getStatus();

    let errorCode = `${status}000`;  
    let errorMessage: string;

    if (status === HttpStatus.NOT_FOUND) {
      if (exception.message.includes('Label')) {
        const id = this.extractIdFromMessage(exception.message);
        errorCode = ErrorCodes.LabelNotFound.toString(); 
        errorMessage = ErrorMessages.LabelNotFoundMessage(id);
      } else if (exception.message.includes('Article')) {
        const id = this.extractIdFromMessage(exception.message);
        errorCode = ErrorCodes.ArticleNotFound.toString();  
        errorMessage = ErrorMessages.ArticleNotFoundMessage(id);
      } else if (exception.message.includes('Note')) {
        const id = this.extractIdFromMessage(exception.message);
        errorCode = ErrorCodes.NoteNotFound.toString();  
        errorMessage = ErrorMessages.NoteNotFoundMessage(id);
      } else {
        errorMessage = exception.message;  
      }
    } else {
      errorMessage = exception.message; 
    }

    response.status(status).json({
      errorCode,
      errorMessage,
    });
  }

  private extractIdFromMessage(message: string): number {
    const match = message.match(/id (\d+)/);
    return match ? Number(match[1]) : 0;
  }
}
