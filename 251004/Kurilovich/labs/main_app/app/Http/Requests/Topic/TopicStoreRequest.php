<?php

namespace App\Http\Requests\Topic;

use App\Http\Requests\ApiRequest;
use Illuminate\Contracts\Validation\Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Http\Exceptions\HttpResponseException;

/**
 * @property int $editorId
 * @property string $title
 * @property string $content
 * @property array $tags
 */
class TopicStoreRequest extends ApiRequest
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
            'title' => 'required|string|max:64|min:2|unique:tbl_topic,title',
            'content' => 'required|string|min:4|max:2048',
            'tags' => 'nullable|array',
            'tags.*' => 'required|string|min:2|max:32',
        ];
    }
}
