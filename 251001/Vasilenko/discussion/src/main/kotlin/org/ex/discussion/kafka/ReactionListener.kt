package org.ex.discussion.kafka

import org.ex.discussion.dto.request.ReactionRequestDTO
import org.ex.discussion.service.ReactionService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ReactionListener(private val reactionService: ReactionService) {

    @KafkaListener(topics = ["\${spring.kafka.topics.reaction.in}"], groupId = "1")
    fun consumeReactionMessage(payload: ReactionRequestDTO) {
        reactionService.createReaction(payload)
    }
}