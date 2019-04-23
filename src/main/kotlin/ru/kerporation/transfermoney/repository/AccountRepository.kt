package ru.kerporation.transfermoney.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.kerporation.transfermoney.domain.Account

@Repository
interface AccountRepository : CrudRepository<Account, Long> {

    fun findAccountById(id: Long): Account?

}
