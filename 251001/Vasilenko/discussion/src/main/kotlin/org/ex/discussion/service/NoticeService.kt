package org.ex.discussion.service

import org.ex.discussion.dto.request.ReactionRequestDTO
import org.ex.discussion.dto.response.ReactionResponseDTO
import org.ex.discussion.exception.NotFoundException
import org.ex.discussion.mapper.ReactionMapper
import org.ex.discussion.repository.ReactionRepository
import org.springframework.stereotype.Service

@Service
class ReactionService(
    private val reactionMapper: ReactionMapper,
    private val reactionRepository: ReactionRepository) {

    fun getAllReactions(): List<ReactionResponseDTO> = reactionRepository.findAll().map { reactionMapper.toDto(it) }

    fun getReactionById(reactionId: Long): ReactionResponseDTO {
        return reactionRepository.findById(reactionId)
            .map { reactionMapper.toDto(it) }
            .orElseThrow {throw NotFoundException("Reaction with id: $reactionId not found")}
    }

    fun createReaction(reactionRequest: ReactionRequestDTO): ReactionResponseDTO {
        val reaction = reactionMapper.toEntity(reactionRequest)
        reactionRepository.save(reaction)
        return reactionMapper.toDto(reaction)
    }

    fun updateReaction(reactionRequest: ReactionRequestDTO): ReactionResponseDTO {
        return reactionRepository.findById(reactionRequest.id)
            .map {
                it.content = reactionRequest.content
                it.tweetId = reactionRequest.tweetId
                val updated = it.copy(content = reactionRequest.content, tweetId = reactionRequest.tweetId)
                return@map reactionMapper.toDto(reactionRepository.save(updated))
            }.orElseThrow {throw NotFoundException("Reaction with id: ${reactionRequest.id}")}
    }

    fun deleteReaction(reactionId: Long): ReactionResponseDTO {
        return reactionRepository.findById(reactionId)
            .map {
                reactionRepository.delete(it.id, it.country)
                return@map it
            }.map { reactionMapper.toDto(it) }
            .orElseThrow{ throw NotFoundException("Reaction with id: $reactionId not found") }
    }
}