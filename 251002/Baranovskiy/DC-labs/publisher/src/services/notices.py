import json
from fastapi import HTTPException
from src.schemas.notices import NoticeResponseTo, NoticeRequestToAdd, NoticeRequestToUpdate
from src.utils.kafka_client import send_kafka_request
import redis.asyncio as redis

global_id_counter = 0

CACHE_TTL = 300

class NoticesService:
    def __init__(self, redis_client: redis.Redis):
        self.redis = redis_client

    async def create_notice(self, notice: NoticeRequestToAdd) -> NoticeResponseTo:
        global global_id_counter
        global_id_counter += 1
        payload = {
            "id": global_id_counter,
            "issueId": notice.issueId,
            "content": notice.content,
        }
        result = await send_kafka_request("POST", payload)
        if result.get("error"):
            raise HTTPException(status_code=400, detail=result["error"])

        await self.redis.delete("notices")
        return NoticeResponseTo(**result)

    async def get_notices(self) -> list[NoticeResponseTo]:
        cached = await self.redis.get("notices")
        if cached:
            notices = json.loads(cached)
            return [NoticeResponseTo(**r) for r in notices]

        result = await send_kafka_request("GET", {})
        if isinstance(result, list):
            await self.redis.set("notices", json.dumps(result), ex=CACHE_TTL)
            return [NoticeResponseTo(**r) for r in result]

        raise HTTPException(status_code=400, detail="Ошибка получения списка реакций")

    async def get_notice_by_id(self, notice_id: int) -> NoticeResponseTo:
        key = f"notice:{notice_id}"
        cached = await self.redis.get(key)
        if cached:
            data = json.loads(cached)
            return NoticeResponseTo(**data)

        payload = {"id": notice_id}
        result = await send_kafka_request("GET_BY_ID", payload)
        if result.get("error"):
            raise HTTPException(status_code=404, detail=result["error"])

        await self.redis.set(key, json.dumps(result), ex=CACHE_TTL)
        return NoticeResponseTo(**result)

    async def update_notice(self, notice: NoticeRequestToUpdate) -> NoticeResponseTo:
        payload = {
            "id": notice.id,
            "issueId": notice.issueId,
            "content": notice.content,
        }
        result = await send_kafka_request("PUT", payload)
        if result.get("error"):
            raise HTTPException(status_code=400, detail=result["error"])

        await self.redis.delete(f"notice:{notice.id}")
        await self.redis.delete("notices")
        return NoticeResponseTo(**result)

    async def delete_notice(self, notice_id: int):
        payload = {"id": notice_id}
        result = await send_kafka_request("DELETE", payload)
        if result.get("error"):
            raise HTTPException(status_code=404, detail=result["error"])

        await self.redis.delete(f"notice:{notice_id}")
        await self.redis.delete("notices")
        return result
