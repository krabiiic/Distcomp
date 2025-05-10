<?php

use App\Http\Controllers\Editor\EditorController;
use Illuminate\Support\Facades\Route;

Route::prefix('editors')
    ->name('editors.')
    ->group(function ($route) {
        $route->get('/', [EditorController::class, 'index'])->name('index');
        $route->post('/', [EditorController::class, 'store'])->name('store');
        $route->get('/{editor}', [EditorController::class, 'show'])->name('show');
        $route->put('/', [EditorController::class, 'update'])->name('update');
        $route->delete('/{editor}', [EditorController::class, 'destroy'])->name('destroy');
    });
