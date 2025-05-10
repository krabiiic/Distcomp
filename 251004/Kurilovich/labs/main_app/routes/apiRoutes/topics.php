<?php

use App\Http\Controllers\Topic\TopicController;
use Illuminate\Support\Facades\Route;

Route::prefix('topics')
    ->name('topics.')
    ->group(function ($route) {
        $route->get('/', [TopicController::class, 'index'])->name('index');
        $route->post('/', [TopicController::class, 'store'])->name('store');
        $route->get('/{topic}', [TopicController::class, 'show'])->name('show');
        $route->put('/{topic}', [TopicController::class, 'update'])->name('update');
        $route->delete('/{topic}', [TopicController::class, 'destroy'])->name('destroy');
    });
