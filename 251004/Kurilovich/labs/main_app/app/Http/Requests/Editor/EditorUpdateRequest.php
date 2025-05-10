<?php

namespace App\Http\Requests\Editor;

use App\Http\Requests\ApiRequest;
use Illuminate\Contracts\Validation\Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Http\Exceptions\HttpResponseException;
use Illuminate\Validation\Rule;

class EditorUpdateRequest extends ApiRequest
{
    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'id' => 'required|int|exists:tbl_editor,id',
            'login' => [
                'required',
                'string',
                'max:64',
                'min:2',
                Rule::unique('tbl_editor', 'login')->ignore($this->id),
            ],
            'password' => 'required|string|min:8|max:128',
            'firstname' => 'required|string|max:64|min:2',
            'lastname' => 'required|string|max:64|min:2',
        ];
    }
}
