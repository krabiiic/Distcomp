<?php

namespace App\Http\Controllers\Topic;

use App\Http\Controllers\Controller;
use App\Http\Requests\Editor\EditorStoreRequest;
use App\Http\Requests\Topic\TopicStoreRequest;
use App\Http\Requests\Topic\TopicUpdateRequest;
use App\Http\Resources\Editor\EditorRecourse;
use App\Http\Resources\Topic\TopicResource;
use App\Models\Editor;
use App\Models\Topic;

class TopicController extends Controller
{
    public function index() {
        $topics = Topic::all();

        return response()->json(TopicResource::collection($topics), 200) ;
    }

    public function store(TopicStoreRequest $request)
    {
        $topic = Topic::create(
            [
                'editor_id' => $request->editorId,
                'title' => $request->title,
                'content' => $request->content,
            ]
        );
        $tags = $request->tags;
        if (!empty($tags)) {
            $tags = array_map(fn ($tag) => ['name' => $tag], $tags);
            $topic->tags()->createMany($tags);
        }

        return response()->json(TopicResource::make($topic), 201);
    }

    public function show(Topic $topic)
    {
        return response()->json(TopicResource::make($topic), 200);
    }

    public function update(TopicUpdateRequest $request, Topic $topic)
    {
        $topic->update([
            'editor_id' => $request->editorId,
            'title' => $request->title,
            'content' => $request->content,
        ]);
        $topic->refresh();

        return response()->json(TopicResource::make($topic), 200);
    }

    public function destroy(Topic $topic)
    {
        $topic->tags()->delete();
        $topic->delete();

        return response()->json(null, 204);
    }
}
