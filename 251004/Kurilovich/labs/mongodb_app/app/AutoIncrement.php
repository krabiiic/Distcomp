<?php

namespace App;

use App\Models\Counter;
use MongoDB\Operation\FindOneAndUpdate;

class AutoIncrement
{
    public static function getNextSequence($name)
    {
        $counter = Counter::raw(function($collection) use ($name) {
            return $collection->findOneAndUpdate(
                ['_id' => $name],
                ['$inc' => ['seq' => 1]],
                [
                    'upsert' => true,
                    'returnDocument' => FindOneAndUpdate::RETURN_DOCUMENT_AFTER
                ]
            );
        });

        return $counter->seq ?? 1;
    }
}
