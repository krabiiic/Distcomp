<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Topic extends Model
{
    public $table = 'tbl_topic';

    public $fillable = [
        'editor_id',
        'title',
        'content',
    ];

    public function tags() {
        return $this->belongsToMany(Tag::class, 'tbl_topic_tag', 'topic_id', 'tag_id');
    }
}
