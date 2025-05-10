package org.ex.discussion.controller

import org.ex.discussion.dto.request.NoticeRequestDTO
import org.ex.discussion.dto.response.NoticeResponseDTO
import org.ex.discussion.service.NoticeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/notices")
class NoticeController(val noticeService: NoticeService) {

    @GetMapping
    fun getAllNotices(): List<NoticeResponseDTO> = noticeService.getAllNotices()

    @GetMapping("/{id}")
    fun getNoticeById(@PathVariable id: Long): NoticeResponseDTO = noticeService.getNoticeById(id)

    @PostMapping
    fun createNotice(@RequestBody noticeRequest: NoticeRequestDTO): ResponseEntity<NoticeResponseDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(noticeRequest))
    }

    @PutMapping
    fun updateNotice(@RequestBody noticeRequest: NoticeRequestDTO): ResponseEntity<NoticeResponseDTO> {
        println("Received updating notice request $noticeRequest")
        return ResponseEntity.ok(noticeService.updateNotice(noticeRequest))
    }

    @DeleteMapping("/{id}")
    fun deleteNotice(@PathVariable("id") noticeId: Long): ResponseEntity<Void> {
        noticeService.deleteNotice(noticeId)
        return ResponseEntity.ok().build()
    }
}