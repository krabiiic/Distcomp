<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Notice extends Model
{
    public $table = 'tbl_notice';

    public $fillable = [
        'topic_id',
        'content',
    ];
}
