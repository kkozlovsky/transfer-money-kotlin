package ru.kerporation.transfermoney.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.kerporation.transfermoney.exception.TransferMoneyException
import ru.kerporation.transfermoney.request.ChangeBalanceRequest
import ru.kerporation.transfermoney.request.TransferMoneyRequest
import ru.kerporation.transfermoney.service.AccountService
import ru.kerporation.transfermoney.service.TransferMoneyService
import javax.validation.Valid

@RestController
class AccountController(private val transferMoneyService: TransferMoneyService,
                        private val accountService: AccountService) {

    @PostMapping(value = ["/change-balance"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(TransferMoneyException::class)
    fun changeBalance(@Valid @RequestBody request: ChangeBalanceRequest): ResponseEntity<String> {
        accountService.changeBalance(request.accountId, request.amount)
        return ResponseEntity.ok().body("Account balance was changed")
    }

    @PostMapping(value = ["/transfer"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Throws(TransferMoneyException::class)
    fun transferMoney(@Valid @RequestBody request: TransferMoneyRequest): ResponseEntity<String> {
        transferMoneyService.transferMoney(request.fromAccountId, request.toAccountId, request.amount)
        return ResponseEntity.ok().body("Money transfer is completed")
    }
}