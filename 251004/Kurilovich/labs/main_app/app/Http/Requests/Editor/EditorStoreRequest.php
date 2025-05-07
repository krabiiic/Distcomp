<?php

namespace App\Http\Requests\Editor;

use App\Http\Requests\ApiRequest;
use Illuminate\Contracts\Validation\Validator;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Http\Exceptions\HttpResponseException;

class EditorStoreRequest extends ApiRequest
{
    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'login' => 'required|string|unique:tbl_editor,login|max:64|min:2',
            'password' => 'required|string|min:8|max:128',
            'firstname' => 'required|string|max:64|min:2',
            'lastname' => 'required|string|max:64|min:2',
        ];
    }
}
