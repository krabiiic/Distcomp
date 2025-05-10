<?php

namespace App\Http\Controllers\Notice;

use App\Http\Controllers\Controller;
use App\Http\Requests\Notice\NoticeStoreRequest;
use App\Http\Requests\Notice\NoticeUpdateRequest;
use App\Http\Resources\Notice\NoticeResource;
use App\Models\Notice;
use GuzzleHttp\Psr7\Request;

class NoticeController extends Controller
{
    public function index() {
        $notices = Notice::all();

        return response()->json(NoticeResource::collection($notices), 200);
    }

    public function store(NoticeStoreRequest $request) {
        $notice = Notice::create(
            [
                'topic_id' => $request->topicId,
                'content' => $request->content,
            ]
        );

        return response()->json(new NoticeResource($notice), 201);
    }

    public function update(NoticeUpdateRequest $request, int $notice)
    {
        $notice = Notice::where('notice_id', $notice)->firstOrFail();

        $notice->update([
            'topic_id' => $request->topicId,
            'content' => $request->content,
        ]);
        $notice->refresh();

        return response()->json(NoticeResource::make($notice), 200);
    }

    public function show(int $notice)
    {
        $notice = Notice::where('notice_id', $notice)->firstOrFail();

        return response()->json(NoticeResource::make($notice), 200);
    }

    public function destroy(int $notice) {
        $notice = Notice::where('notice_id', $notice)->firstOrFail();
        $notice->delete();

        return response()->json(null, 204);
    }
}
