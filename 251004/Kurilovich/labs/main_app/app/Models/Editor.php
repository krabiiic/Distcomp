<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Casts\Attribute;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Hash;

class Editor extends Model
{
    public $table = 'tbl_editor';

    public $fillable = [
        'login',
        'password',
        'firstname',
        'lastname',
    ];

    protected function password(): Attribute
    {
        return Attribute::make(
            set: fn($value) => Hash::make($value)
        );
    }
}
