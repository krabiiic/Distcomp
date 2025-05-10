<?php

namespace App\Http\Requests\Notice;

use App\Http\Requests\ApiRequest;
use Illuminate\Foundation\Http\FormRequest;

class NoticeStoreRequest extends ApiRequest
{
    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'topicId' => 'required|integer',
            'content' => 'required|string|min:4|max:2048',
        ];
    }
}
