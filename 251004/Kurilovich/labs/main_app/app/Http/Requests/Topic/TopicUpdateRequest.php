<?php

namespace App\Http\Requests\Topic;

use App\Http\Requests\ApiRequest;
use Illuminate\Contracts\Validation\Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Http\Exceptions\HttpResponseException;
use Illuminate\Validation\Rule;

/**
 * @property int $id
 * @property int $editorId
 * @property string $title
 * @property string $content
 */
class TopicUpdateRequest extends ApiRequest
{
    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'editorId' => 'required|integer|exists:tbl_editor,id',
            'title' => [
                'required',
                'string',
                'max:64',
                'min:2',
                Rule::unique('tbl_topic', 'title')->ignore($this->topic->id)
            ],
            'content' => 'required|string|min:4|max:2048',
        ];
    }
}
