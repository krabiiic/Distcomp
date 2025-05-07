<?php

use App\Http\Controllers\Editor\EditorController;
use App\Http\Controllers\Notice\NoticeController;
use Illuminate\Support\Facades\Route;

Route::prefix('notices')
    ->name('notices.')
    ->group(function ($route) {
        $route->get('/', [NoticeController::class, 'index'])->name('index');
        $route->post('/', [NoticeController::class, 'store'])->name('store');
        $route->get('/{id}', [NoticeController::class, 'show'])->name('show');
        $route->put('/{id}', [NoticeController::class, 'update'])->name('update');
        $route->delete('/{id}', [NoticeController::class, 'destroy'])->name('destroy');
    });
