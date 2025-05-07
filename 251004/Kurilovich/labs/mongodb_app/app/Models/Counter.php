<?php

namespace App\Models;

// use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use MongoDB\Laravel\Eloquent\Model;

class Counter extends Model
{
    protected $connection = 'mongodb';
    protected $collection = 'counters'; // коллекция в MongoDB
    protected $fillable = ['_id', 'seq'];
    public $timestamps = false;
}
