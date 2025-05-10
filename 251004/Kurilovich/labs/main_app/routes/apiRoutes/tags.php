<?php

use App\Http\Controllers\Editor\EditorController;
use App\Http\Controllers\Notice\NoticeController;
use App\Http\Controllers\Tag\TagController;
use Illuminate\Support\Facades\Route;

Route::prefix('tags')
    ->name('tags.')
    ->group(function ($route) {
        $route->get('/', [TagController::class, 'index'])->name('index');
        $route->post('/', [TagController::class, 'store'])->name('store');
        $route->get('/{tag}', [TagController::class, 'show'])->name('show');
        $route->put('/{tag}', [TagController::class, 'update'])->name('update');
        $route->delete('/{tag}', [TagController::class, 'destroy'])->name('destroy');
    });
