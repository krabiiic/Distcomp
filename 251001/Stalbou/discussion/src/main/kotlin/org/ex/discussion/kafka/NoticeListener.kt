package org.ex.discussion.kafka

import org.ex.discussion.dto.request.NoticeRequestDTO
import org.ex.discussion.service.NoticeService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class NoticeListener(private val noticeService: NoticeService) {

    @KafkaListener(topics = ["\${spring.kafka.topics.notice.in}"], groupId = "1")
    fun consumeNoticeMessage(payload: NoticeRequestDTO) {
        noticeService.createNotice(payload)
    }
}