<?php

namespace App\Providers;

use Illuminate\Support\Facades\Http;
use Illuminate\Support\ServiceProvider;

class AppServiceProvider extends ServiceProvider
{
    /**
     * Register any application services.
     */
    public function register(): void
    {
        //
    }

    /**
     * Bootstrap any application services.
     */
    public function boot(): void
    {
        Http::macro('mongo', function () {
            return Http::withHeaders([
                'Content-Type' => 'application/json',
                'Accept' => 'application/json',
            ])->baseUrl('http://mongodb-php:8000/api/v1.0/notices');
        });
    }
}
