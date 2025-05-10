<?php

namespace App\Http\Controllers\Tag;

use App\Http\Controllers\Controller;
use App\Http\Requests\Tag\TagStoreRequest;
use App\Http\Requests\Tag\TagUpdateRequest;
use App\Http\Resources\Tag\TagResource;
use App\Models\Tag;
use Illuminate\Http\Request;

class TagController extends Controller
{
    public function index() {
        $tags = Tag::all();

        return response()->json(TagResource::collection($tags), 200);
    }

    public function store(TagStoreRequest $request)
    {
        $tag = Tag::create($request->validated());

        return response()->json(TagResource::make($tag), 201);
    }

    public function show(Tag $tag) {
        return response()->json(TagResource::make($tag), 200);
    }

    public function update(Tag $tag, TagUpdateRequest $request)
    {
        $tag->update($request->validated());

        return response()->json(TagResource::make($tag), 200);
    }

    public function destroy(Tag $tag)
    {
        $tag->delete();

        return response()->json(null, 204);
    }
}
