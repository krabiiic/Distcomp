<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Tag extends Model
{
    public $table = 'tbl_tag';

    public $fillable = [
        'name',
    ];
}
