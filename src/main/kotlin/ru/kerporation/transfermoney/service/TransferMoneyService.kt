package ru.kerporation.transfermoney.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.kerporation.transfermoney.exception.TransferMoneyException
import java.math.BigDecimal

@Service
class TransferMoneyService(private val accountService: AccountService) {

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = [TransferMoneyException::class])
    @Throws(TransferMoneyException::class)
    fun transferMoney(fromAccountId: Long, toAccountId: Long, amount: BigDecimal) {
        if (fromAccountId == toAccountId) {
            throw TransferMoneyException("Accounts ids are identical")
        }
        val minAccount = if (fromAccountId < toAccountId) fromAccountId else toAccountId
        val maxAccount = if (fromAccountId < toAccountId) toAccountId else fromAccountId
        val transferAmount = if (fromAccountId < toAccountId) amount.negate() else amount

        accountService.changeBalance(minAccount, transferAmount)
        accountService.changeBalance(maxAccount, transferAmount.negate())
    }

}