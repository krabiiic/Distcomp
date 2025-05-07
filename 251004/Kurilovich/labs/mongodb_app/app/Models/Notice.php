<?php

namespace App\Models;

use App\AutoIncrement;
use MongoDB\Laravel\Eloquent\Model;

class Notice extends Model
{
    protected $connection = 'mongodb';
    protected $table = 'notice';
    protected $collection = 'notice';
    protected $fillable = [
        'content',
        'topic_id',
        'notice_id',
    ];

    protected static function booted()
    {
        static::creating(function ($notice) {
            $notice->notice_id = AutoIncrement::getNextSequence('notice');
        });
    }
}
