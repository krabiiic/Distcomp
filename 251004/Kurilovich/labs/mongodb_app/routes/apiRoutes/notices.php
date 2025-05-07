<?php

use App\Http\Controllers\Notice\NoticeController;
use Illuminate\Support\Facades\Route;

Route::prefix('notices')
    ->name('notices.')
    ->group(function ($route) {
        $route->get('/', [NoticeController::class, 'index'])->name('index');
        $route->post('/', [NoticeController::class, 'store'])->name('store');
        $route->get('/{notice}', [NoticeController::class, 'show'])->name('show');
        $route->put('/{notice}', [NoticeController::class, 'update'])->name('update');
        $route->delete('/{notice}', [NoticeController::class, 'destroy'])->name('destroy');
    });
