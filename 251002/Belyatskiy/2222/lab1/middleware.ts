import { plainToInstance } from 'class-transformer';
import { validate, ValidationError } from 'class-validator';
import { Request, Response, NextFunction } from 'express';

export function validationMiddleware<T extends object>(dtoClass: new () => T, error) {
    return async (req: Request, res: Response, next: NextFunction) => {
        const dtoInstance = plainToInstance(dtoClass, req.body);

        const errors: ValidationError[] = await validate(dtoInstance);

        if (errors.length > 0) {
            const validationErrors = errors.map((error) => {
                return {
                    property: error.property,
                    constraints: error.constraints,
                };
            });

            return res.status(400).json({
                message: error,
                errors: validationErrors,
            });
        }

        req.body = dtoInstance;
        next();
    };
}