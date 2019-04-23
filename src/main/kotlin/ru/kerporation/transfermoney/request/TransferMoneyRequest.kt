package ru.kerporation.transfermoney.request

import java.math.BigDecimal
import javax.validation.constraints.DecimalMin

data class TransferMoneyRequest(
        val fromAccountId: Long,
        val toAccountId: Long,
        @DecimalMin("0.01") val amount: BigDecimal
)