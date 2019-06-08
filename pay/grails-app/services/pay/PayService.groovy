package pay

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.json.JsonSlurper

import java.text.SimpleDateFormat

@GrailsCompileStatic
@Transactional
class PayService {
	CardService cardService
	BankService bankService
	
	String pay(String tenant, PayCommand cmd) {
		if (cmd.type == "card") {
			return payCard(tenant, cmd.cardNumber, cmd.cardCvv, cmd.cardExpMonth, cmd.cardExpYear, cmd.amount)
		} else if (cmd.type == "bank") {
			return payBank(tenant, cmd.bankRouting, cmd.bankAccount, cmd.amount)
		} else if (cmd.type == "saved") {
			return paySaved(tenant, cmd.savedId, cmd.amount)
		} else {
			throw new RuntimeException(cmd.type)
		}
	}
	
	private String payCard(String tenant, long number, long cvv, long expMonth, long expYear, BigDecimal amount) {
		Card card = cardService.lookupCard(tenant, number, true)
		
		if (!card.present) {
			throw new InvalidCardException()
		}
		
		assert card.number == number
		if ((card.cvv != null && card.cvv != cvv) ||
				(card.expMonth != null && card.expMonth != expMonth) ||
				(card.expYear != null && card.expYear != expYear)) {
			throw new DeclinedException()
		}
		
		SimpleDateFormat yearDateFormat = new SimpleDateFormat("yy")
		int nowYear = Integer.parseInt(yearDateFormat.format(Calendar.getInstance().getTime()))
		SimpleDateFormat monthDataFormat = new SimpleDateFormat("MM")
		int nowMonth = Integer.parseInt(monthDataFormat.format(Calendar.getInstance().getTime()))
		if (nowYear > expYear ||
				(nowYear == expYear && nowMonth > expMonth)) {
			throw new ExpiredException()
		}
		
		card.balance += amount
		card.save(flush: true)
		
		return UUID.randomUUID().toString()
	}
	
	private String payBank(String tenant, long routing, long account, BigDecimal amount) {
		BankAccount bankAccount = bankService.lookupAccount(tenant, routing, account, true)
		
		if (!bankAccount.bankRouting.present && !bankAccount.bankRouting.deferErrors) {
			throw new InvalidRoutingException()
		}
		if (!bankAccount.present && !bankAccount.deferErrors) {
			throw new InvalidAccountException()
		}
		
		bankAccount.balance += amount
		
		BankCharge bankCharge = new BankCharge()
		bankAccount.addToCharges(bankCharge)
		
		bankAccount.save(flush: true)
		
		return bankCharge.chargeId
	}
	
	private String paySaved(String tenant, String savedId, BigDecimal amount) {
		Map saved = new JsonSlurper().parseText(savedId) as Map
		
		if (saved.t == "card") {
			return payCard(tenant, saved.n as Long, saved.c as Long, saved.m as Long, saved.y as Long, amount)
		} else if (saved.t == "bank") {
			return payBank(tenant, saved.r as Long, saved.a as Long, amount)
		} else {
			throw new RuntimeException(saved.t as String)
		}
	}
}

/**
 * Thrown when the card number does not exist.
 */
class InvalidCardException extends Exception {}
/**
 * Thrown when the routing number is invalid.
 */
class InvalidRoutingException extends Exception {}
/**
 * Thrown when the account number is invalid.
 */
class InvalidAccountException extends Exception {}
/**
 * Thrown when the card has expired.
 */
class ExpiredException extends Exception {}
/**
 * Thrown when the payment was declined (caused by incorrect CVV number or expiration date).
 */
class DeclinedException extends Exception {}
