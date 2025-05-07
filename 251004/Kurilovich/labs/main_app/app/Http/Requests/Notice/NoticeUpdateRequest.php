<?php

namespace App\Http\Requests\Notice;

use App\Http\Requests\ApiRequest;
use Illuminate\Contracts\Validation\Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Http\Exceptions\HttpResponseException;


/**
 * @property int $topicId
 * @property string $content
 */
class NoticeUpdateRequest extends ApiRequest
{
    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'topicId' => 'required|integer|exists:tbl_topic,id',
            'content' => 'required|string|min:4|max:2048',
        ];
    }
}
