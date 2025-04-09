package by.kukhatskavolets.controllers

import by.kukhatskavolets.dto.requests.UserRequestTo
import by.kukhatskavolets.dto.responses.UserResponseTo
import by.kukhatskavolets.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1.0/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@RequestBody @Valid userRequestTo: UserRequestTo): ResponseEntity<UserResponseTo> {
        val createdUser = userService.createUser(userRequestTo)
        return ResponseEntity(createdUser, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponseTo> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponseTo>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @PutMapping()
    fun updateUser(@RequestBody @Valid userRequestTo: UserRequestTo): ResponseEntity<UserResponseTo> {
        val updatedUser = userService.updateUser(userRequestTo.id, userRequestTo)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/by-tweet/{tweetId}")
    fun getUserByTweetId(@PathVariable tweetId: Long): ResponseEntity<UserResponseTo> {
        val user = userService.getUserByTweetId(tweetId)
        return ResponseEntity.ok(user)
    }
}