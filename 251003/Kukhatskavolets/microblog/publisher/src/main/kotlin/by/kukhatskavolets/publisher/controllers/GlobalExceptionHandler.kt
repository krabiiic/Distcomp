package by.kukhatskavolets.publisher.controllers

import by.kukhatskavolets.publisher.dto.responses.ExceptionResponseTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.sql.SQLException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException): ResponseEntity<ExceptionResponseTo> {
        val httpStatus = HttpStatus.NOT_FOUND
        val errorCode = generateErrorCode(httpStatus, 1)
        val response = ExceptionResponseTo(errorCode, "Resource not found: ${ex.message}")
        return ResponseEntity(response, httpStatus)
    }

    @ExceptionHandler(SQLException::class)
    fun handlePSQLException(ex: SQLException): ResponseEntity<ExceptionResponseTo> {
        val httpStatus = HttpStatus.FORBIDDEN
        val errorCode = generateErrorCode(httpStatus, 2)
        val response = ExceptionResponseTo(errorCode, "Database access error: ${ex.message}")
        return ResponseEntity(response, httpStatus)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionResponseTo> {
        val httpStatus = HttpStatus.BAD_REQUEST
        val errorCode = generateErrorCode(httpStatus, 1) // 40001

        val errorMessage = ex.bindingResult.fieldErrors
            .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }

        val response = ExceptionResponseTo(errorCode, "Validation failed: $errorMessage")
        return ResponseEntity(response, httpStatus)
    }

    private fun generateErrorCode(httpStatus: HttpStatus, logicalCode: Int): Int {
        return "${httpStatus.value()}%02d".format(logicalCode).toInt()
    }
}

