package ru.kerporation.transfermoney.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.kerporation.transfermoney.exception.TransferMoneyException
import ru.kerporation.transfermoney.repository.AccountRepository
import java.math.BigDecimal
import java.util.*

@Service
class AccountService(private val accountRepository: AccountRepository) {

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = [TransferMoneyException::class])
    @Throws(TransferMoneyException::class)
    fun changeBalance(id: Long, amount: BigDecimal) {
        val account = accountRepository.findAccountById(id)
        if (Objects.isNull(account)) {
            throw TransferMoneyException("Account id: $id not found")
        }
        val newBalance = account!!.balance.add(amount)
        if (newBalance < BigDecimal.ZERO) {
            throw TransferMoneyException("Not enough money in account: $id")
        }
        account.balance = newBalance
    }

}