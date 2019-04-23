package ru.kerporation.transfermoney.controller.advice

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.kerporation.transfermoney.exception.TransferMoneyException

@ControllerAdvice
class TransferMoneyExceptionHandler {

    @ExceptionHandler(TransferMoneyException::class)
    fun handleException(e: Exception): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }
}