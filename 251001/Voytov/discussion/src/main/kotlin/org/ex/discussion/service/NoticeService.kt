package org.ex.discussion.service

import org.ex.discussion.dto.request.NoticeRequestDTO
import org.ex.discussion.dto.response.NoticeResponseDTO
import org.ex.discussion.exception.NotFoundException
import org.ex.discussion.mapper.NoticeMapper
import org.ex.discussion.repository.NoticeRepository
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeMapper: NoticeMapper,
    private val noticeRepository: NoticeRepository) {

    fun getAllNotices(): List<NoticeResponseDTO> = noticeRepository.findAll().map { noticeMapper.toDto(it) }

    fun getNoticeById(noticeId: Long): NoticeResponseDTO {
        return noticeRepository.findById(noticeId)
            .map { noticeMapper.toDto(it) }
            .orElseThrow {throw NotFoundException("Notice with id: $noticeId not found")}
    }

    fun createNotice(noticeRequest: NoticeRequestDTO): NoticeResponseDTO {
        val notice = noticeMapper.toEntity(noticeRequest)
        noticeRepository.save(notice)
        return noticeMapper.toDto(notice)
    }

    fun updateNotice(noticeRequest: NoticeRequestDTO): NoticeResponseDTO {
        return noticeRepository.findById(noticeRequest.id)
            .map {
                it.content = noticeRequest.content
                it.newsId = noticeRequest.newsId
                val updated = it.copy(content = noticeRequest.content, newsId = noticeRequest.newsId)
                return@map noticeMapper.toDto(noticeRepository.save(updated))
            }.orElseThrow {throw NotFoundException("Notice with id: ${noticeRequest.id}")}
    }

    fun deleteNotice(noticeId: Long): NoticeResponseDTO {
        return noticeRepository.findById(noticeId)
            .map {
                noticeRepository.delete(it.id, it.country)
                return@map it
            }.map { noticeMapper.toDto(it) }
            .orElseThrow{ throw NotFoundException("Notice with id: $noticeId not found") }
    }
}