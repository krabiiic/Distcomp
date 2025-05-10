import json
from aiokafka import AIOKafkaProducer, AIOKafkaConsumer
from src.repositories import notices
import asyncio

KAFKA_BOOTSTRAP_SERVERS = "broker:29092"
INTOPIC = "InTopic"
OUTTOPIC = "OutTopic"


async def init_producer():
    producer = AIOKafkaProducer(
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        value_serializer=lambda v: json.dumps(v).encode("utf-8"),
        key_serializer=lambda k: str(k).encode("utf-8"),
    )
    await producer.start()
    return producer


async def process_message(message: dict) -> dict:
    method = message.get("method")
    payload = message.get("payload", {})
    response = {"correlation_id": message.get("correlation_id")}
    try:
        if method == "POST":
            moderated_state = moderate_notice(payload.get("content", ""))
            result = notices.create_notice(payload["id"], payload["issueId"], payload["content"], moderated_state)
            response["result"] = result
        elif method == "GET":
            result = notices.get_all_notices()
            response["result"] = result
        elif method == "GET_BY_ID":
            result = notices.get_notice_by_id(payload["id"])
            if not result:
                response["result"] = {"error": "Notice not found"}
            else:
                response["result"] = result
        elif method == "PUT":
            moderated_state = moderate_notice(payload.get("content", ""))
            result = notices.update_notice(payload["id"], payload["issueId"], payload["content"], moderated_state)
            response["result"] = result
        elif method == "DELETE":
            result = notices.get_notice_by_id(payload["id"])
            if not result:
                response["result"] = {"error": "Notice not found"}
            else:
                notices.delete_notice(payload["id"])
                response["result"] = {"status": "deleted"}
        else:
            response["result"] = {"error": "Invalid method"}
    except Exception as e:
        response["result"] = {"error": str(e)}
    return response


def moderate_notice(content: str) -> str:
    stop_words = ["badword1", "badword2"]
    content_lower = content.lower()
    if any(sw in content_lower for sw in stop_words):
        return "DECLINE"
    return "APPROVE"


async def kafka_request_consumer():
    consumer = AIOKafkaConsumer(
        INTOPIC,
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        value_deserializer=lambda v: json.loads(v.decode("utf-8")),
        key_deserializer=lambda k: k.decode("utf-8") if k else None,
        group_id="discussion_group",
    )
    await consumer.start()
    producer = await init_producer()
    try:
        async for msg in consumer:
            request = msg.value
            response = await process_message(request)
            key = request.get("payload", {}).get("issueId", request.get("correlation_id"))
            await producer.send(OUTTOPIC, value=response, key=key)
    finally:
        await consumer.stop()
        await producer.stop()


async def start_kafka_consumer():
    asyncio.create_task(kafka_request_consumer())
