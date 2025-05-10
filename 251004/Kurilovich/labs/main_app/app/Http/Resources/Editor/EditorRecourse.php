<?php

namespace App\Http\Resources\Editor;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class EditorRecourse extends JsonResource
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
            'login' => $this->login,
            'password' => $this->password,
            'firstname' => $this->firstname,
            'lastname' => $this->lastname
        ];
    }
}
