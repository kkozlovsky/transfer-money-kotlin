package ru.kerporation.transfermoney.controller

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import ru.kerporation.transfermoney.repository.AccountRepository
import ru.kerporation.transfermoney.request.ChangeBalanceRequest
import ru.kerporation.transfermoney.request.TransferMoneyRequest
import java.math.BigDecimal

@RunWith(SpringRunner::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var repository: AccountRepository

    @Test
    fun addMoneySuccessTest() {
        val request = ChangeBalanceRequest(1L, BigDecimal(100))

        val amountBefore = repository.findAccountById(request.accountId)!!.balance
        assertEquals(0, amountBefore.compareTo(BigDecimal(1000)).toLong())

        val response = restTemplate.postForEntity("/change-balance", request, String::class.java)
        assertThat<HttpStatus>(response.statusCode, equalTo<HttpStatus>(HttpStatus.OK))
        assertThat<String>(response.body, equalTo<String>("Account balance was changed"))
        val amountAfter = repository.findAccountById(request.accountId)!!.balance
        assertEquals(0, amountAfter.compareTo(BigDecimal(1100)).toLong())
    }

    @Test
    fun withdrawMoneySuccessTest() {
        val request = ChangeBalanceRequest(1L, BigDecimal(1000).negate())

        val amountBefore = repository.findAccountById(request.accountId)!!.balance
        assertEquals(0, amountBefore.compareTo(BigDecimal(1000)).toLong())

        val response = restTemplate.postForEntity("/change-balance", request, String::class.java)
        assertThat<HttpStatus>(response.statusCode, equalTo<HttpStatus>(HttpStatus.OK))
        assertThat<String>(response.body, equalTo<String>("Account balance was changed"))
        val amountAfter = repository.findAccountById(request.accountId)!!.balance
        assertEquals(0, amountAfter.compareTo(BigDecimal.ZERO).toLong())
    }

    @Test
    fun notEnoughMoneyTest() {
        val request = ChangeBalanceRequest(1L, BigDecimal(1001).negate())

        val amountBefore = repository.findAccountById(request.accountId)!!.balance
        assertEquals(0, amountBefore.compareTo(BigDecimal(1000)).toLong())

        val response = restTemplate.postForEntity("/change-balance", request, String::class.java)
        assertThat<HttpStatus>(response.statusCode, equalTo<HttpStatus>(HttpStatus.BAD_REQUEST))
        assertThat<String>(response.body, equalTo<String>("Not enough money in account: 1"))
        val amountAfter = repository.findAccountById(request.accountId)!!.balance
        assertEquals(0, amountAfter.compareTo(amountBefore).toLong())
    }

    @Test
    fun accountNotFoundTest() {
        val request = ChangeBalanceRequest(4L, BigDecimal(100).negate())

        val response = restTemplate.postForEntity("/change-balance", request, String::class.java)
        assertThat<HttpStatus>(response.statusCode, equalTo<HttpStatus>(HttpStatus.BAD_REQUEST))
        assertThat<String>(response.body, equalTo<String>("Account id: 4 not found"))
    }

    @Test
    fun transferMoneyCompletedTest() {
        val request = TransferMoneyRequest(1L, 2L, BigDecimal(1000))

        val amountBeforeFromAccount = repository.findAccountById(request.fromAccountId)!!.balance
        assertEquals(0, amountBeforeFromAccount.compareTo(BigDecimal(1000)).toLong())
        val amountBeforeToAccount = repository.findAccountById(request.toAccountId)!!.balance
        assertEquals(0, amountBeforeToAccount.compareTo(BigDecimal(2000)).toLong())

        val response = restTemplate.postForEntity("/transfer", request, String::class.java)
        assertThat<HttpStatus>(response.statusCode, equalTo<HttpStatus>(HttpStatus.OK))
        assertThat<String>(response.body, equalTo<String>("Money transfer is completed"))

        val amountAfterFromAccount = repository.findAccountById(request.fromAccountId)!!.balance
        assertEquals(0, amountAfterFromAccount.compareTo(BigDecimal.ZERO).toLong())
        val amountAfterToAccount = repository.findAccountById(request.toAccountId)!!.balance
        assertEquals(0, amountAfterToAccount.compareTo(BigDecimal(3000)).toLong())
    }

    @Test
    fun transferMoneyNotEnoughMoneyTest() {
        val request = TransferMoneyRequest(1L, 2L, BigDecimal(1001))

        val amountBeforeFromAccount = repository.findAccountById(request.fromAccountId)!!.balance
        assertEquals(0, amountBeforeFromAccount.compareTo(BigDecimal(1000)).toLong())
        val amountBeforeToAccount = repository.findAccountById(request.toAccountId)!!.balance
        assertEquals(0, amountBeforeToAccount.compareTo(BigDecimal(2000)).toLong())

        val response = restTemplate.postForEntity("/transfer", request, String::class.java)
        assertThat<HttpStatus>(response.statusCode, equalTo<HttpStatus>(HttpStatus.BAD_REQUEST))
        assertThat<String>(response.body, equalTo<String>("Not enough money in account: 1"))

        val amountAfterFromAccount = repository.findAccountById(request.fromAccountId)!!.balance
        assertEquals(0, amountAfterFromAccount.compareTo(amountBeforeFromAccount).toLong())
        val amountAfterToAccount = repository.findAccountById(request.toAccountId)!!.balance
        assertEquals(0, amountAfterToAccount.compareTo(amountBeforeToAccount).toLong())
    }

    @Test
    fun transferMoneyAccountNotFoundTest() {
        val request = TransferMoneyRequest(1L, 4L, BigDecimal(100))

        val amountBeforeFromAccount = repository.findAccountById(request.fromAccountId)!!.balance
        assertEquals(0, amountBeforeFromAccount.compareTo(BigDecimal(1000)).toLong())

        val response = restTemplate.postForEntity("/transfer", request, String::class.java)
        assertThat<HttpStatus>(response.statusCode, equalTo<HttpStatus>(HttpStatus.BAD_REQUEST))
        assertThat<String>(response.body, equalTo<String>("Account id: 4 not found"))

        val amountAfterFromAccount = repository.findAccountById(request.fromAccountId)!!.balance
        assertEquals(0, amountAfterFromAccount.compareTo(amountBeforeFromAccount).toLong())
    }


    @Test
    fun transferMoneyBetweenAccountTest() {
        val request = TransferMoneyRequest(1L, 1L, BigDecimal(100))

        val response = restTemplate.postForEntity("/transfer", request, String::class.java)
        assertThat<HttpStatus>(response.statusCode, equalTo<HttpStatus>(HttpStatus.BAD_REQUEST))
        assertThat<String>(response.body, equalTo<String>("Accounts ids are identical"))
    }
}