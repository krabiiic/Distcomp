import { Request, Response, NextFunction } from 'express';

export const errorHandler = (err: Error, req: Request, res: Response, next: NextFunction) => {
    console.error(err.stack);

    // Обработка ошибок валидации
    if (err.message === 'Validation failed') {
        return res.status(400).json({ message: 'Validation error', details: err.message });
    }

    // Обработка других ошибок
    res.status(500).json({ message: 'Internal server error' });
};