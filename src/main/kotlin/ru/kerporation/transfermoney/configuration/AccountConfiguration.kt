package ru.kerporation.transfermoney.configuration

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.kerporation.transfermoney.domain.Account
import ru.kerporation.transfermoney.repository.AccountRepository
import java.math.BigDecimal

@Configuration
class AccountConfiguration {

    @Bean
    fun databaseInitializer(accountRepository: AccountRepository) = ApplicationRunner {
        val account1 = Account(ownerName = "Kirill", balance = BigDecimal(1000))
        val account2 = Account(ownerName = "Max", balance = BigDecimal(2000))
        val account3 = Account(ownerName = "Ivan", balance = BigDecimal(5000))

        accountRepository.save(account1)
        accountRepository.save(account2)
        accountRepository.save(account3)
    }

}


