<?php

namespace App\Http\Controllers\Notice;

use App\Http\Controllers\Controller;
use App\Http\Requests\Notice\NoticeStoreRequest;
use App\Http\Requests\Notice\NoticeUpdateRequest;
use App\Http\Requests\Topic\TopicStoreRequest;
use App\Http\Requests\Topic\TopicUpdateRequest;
use App\Http\Resources\Notice\NoticeResource;
use App\Http\Resources\Topic\TopicResource;
use App\Models\Editor;
use App\Models\Notice;
use App\Models\Topic;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Http;

class NoticeController extends Controller
{
    public function index()
    {
        $notices = Http::mongo()->get('/')->json();

        return response()->json($notices, 200);
    }

    public function store(NoticeStoreRequest $request)
    {
        $notice = Http::mongo()->post('/', [
            'topicId' => $request->topicId,
            'content' => $request->content,
        ])->json();
        Cache::put('notice-'.$notice['id'], $notice, 60);

        return response()->json($notice, 201);
    }

    public function update(NoticeUpdateRequest $request, string $id)
    {
        $notice = Http::mongo()->put('/'.$id, [
            'topicId' => $request->topicId,
            'content' => $request->content,
        ])->json();
        Cache::put('notice-'.$notice['id'], $notice, 60);

        return response()->json($notice, 200);
    }

    public function show(string $id)
    {
        if (!$notice = Cache::get('notice-'.$id)) {
            $notice = Http::mongo()->get('/'.$id)->json();
        }


        return response()->json($notice, 200);
    }

    public function destroy(string $id) {
        Http::mongo()->delete('/'.$id)->json();

        return response()->json(null, 204);
    }
}
