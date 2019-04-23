package ru.kerporation.transfermoney.request

import java.math.BigDecimal

data class ChangeBalanceRequest(
        val accountId: Long,
        val amount: BigDecimal
)