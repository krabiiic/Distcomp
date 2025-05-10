<?php

namespace App\Http\Resources\Notice;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class NoticeResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id' => $this->notice_id,
            'content' => $this->content,
            'topicId' => $this->topic_id
        ];
    }
}
