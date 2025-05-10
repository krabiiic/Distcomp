package by.kukhatskavolets.publisher.controllers

import by.kukhatskavolets.publisher.dto.requests.MarkRequestTo
import by.kukhatskavolets.publisher.dto.responses.MarkResponseTo
import by.kukhatskavolets.publisher.services.MarkService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1.0/marks")
class MarkController(
    private val markService: MarkService
) {

    @PostMapping
    fun createMark(@RequestBody @Valid markRequestTo: MarkRequestTo): ResponseEntity<MarkResponseTo> {
        val createdMark = markService.createMark(markRequestTo)
        return ResponseEntity(createdMark, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getMarkById(@PathVariable id: Long): ResponseEntity<MarkResponseTo> {
        val mark = markService.getMarkById(id)
        return ResponseEntity.ok(mark)
    }

    @GetMapping
    fun getAllMarks(): ResponseEntity<List<MarkResponseTo>> {
        val marks = markService.getAllMarks()
        return ResponseEntity.ok(marks)
    }

    @PutMapping()
    fun updateMark(@RequestBody @Valid markRequestTo: MarkRequestTo): ResponseEntity<MarkResponseTo> {
        val updatedMark = markService.updateMark(markRequestTo.id, markRequestTo)
        return ResponseEntity.ok(updatedMark)
    }

    @DeleteMapping("/{id}")
    fun deleteMark(@PathVariable id: Long): ResponseEntity<Void> {
        markService.deleteMark(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/by-tweet/{tweetId}")
    fun getMarksByTweetId(@PathVariable tweetId: Long): ResponseEntity<List<MarkResponseTo>> {
        val marks = markService.getMarksByTweetId(tweetId)
        return ResponseEntity.ok(marks)
    }
}
