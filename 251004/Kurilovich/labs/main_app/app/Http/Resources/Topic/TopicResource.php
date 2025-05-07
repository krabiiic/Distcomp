<?php

namespace App\Http\Resources\Topic;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class TopicResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id' => $this->id,
            'editorId' => $this->editor_id,
            'title' => $this->title,
            'content' => $this->content,
            'created' => $this->created_at,
            'modified' => $this->updated_at
        ];
    }
}
