<?php

use Illuminate\Foundation\Application;
use Illuminate\Foundation\Configuration\Exceptions;
use Illuminate\Foundation\Configuration\Middleware;

return Application::configure(basePath: dirname(__DIR__))
    ->withRouting(
        web: __DIR__ . '/../routes/web.php',
        api: __DIR__ . '/../routes/api.php',
        commands: __DIR__ . '/../routes/console.php',
        health: '/up',
        apiPrefix: '/api/v1.0',
    )
    ->withMiddleware(function (Middleware $middleware) {
        //
    })
    ->withExceptions(function (Exceptions $exceptions) {
        $exceptions->render(function (Throwable $e) {
            $code = null;
            if (method_exists($e, 'getStatusCode')) {
                $code = $e->getStatusCode();
            }
            return response()->json([
                'message' => $e->getMessage(),
                'trace' => $e->getTrace(),
            ], $code ?? 500);
        });
    })->create();
