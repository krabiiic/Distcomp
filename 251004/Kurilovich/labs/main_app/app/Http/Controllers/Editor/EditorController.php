<?php

namespace App\Http\Controllers\Editor;

use App\Http\Controllers\Controller;
use App\Http\Requests\Editor\EditorStoreRequest;
use App\Http\Requests\Editor\EditorUpdateRequest;
use App\Http\Resources\Editor\EditorRecourse;
use App\Models\Editor;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Cache;

class EditorController extends Controller
{
    public function index() {
        $editors = Editor::all();

        return response()->json(EditorRecourse::collection($editors), 200) ;
    }

    public function store(EditorStoreRequest $request)
    {
        $editor = Editor::create($request->validated());

        return response()->json(EditorRecourse::make($editor), 201);
    }

    public function show(Editor $editor) {
        return response()->json(EditorRecourse::make($editor), 200);
    }

    public function update(EditorUpdateRequest $request) {
        $validatedData = $request->validated();
        $editor = Editor::findOrFail($validatedData['id']);
        $editor->update($validatedData);
        $editor->refresh();

        return response()->json(EditorRecourse::make($editor), 200);
    }

    public function destroy(Editor $editor) {
        $editor->delete();

        return response()->json(null, 204);
    }
}
