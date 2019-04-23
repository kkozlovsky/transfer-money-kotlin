package ru.kerporation.transfermoney.domain

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
        @Id @GeneratedValue @Column(name = "id", nullable = false) var id: Long? = null,
        @Column(name = "owner_name", nullable = false) var ownerName: String,
        @Column(name = "balance", nullable = false) var balance: BigDecimal
)

