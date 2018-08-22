package pay

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

import java.text.SimpleDateFormat

@GrailsCompileStatic
@Transactional
class PayService {
	CardService cardService
	BankService bankService
	
	void pay(String tenant, PayCommand cmd) {
		if (cmd.type == "card") {
			payCard(tenant, cmd.cardNumber, cmd.cardCvv, cmd.cardExpMonth, cmd.cardExpYear, cmd.amount)
		} else if (cmd.type == "bank") {
			payBank(tenant, cmd.bankRouting, cmd.bankAccount, cmd.amount)
		} else if (cmd.type == "saved") {
			paySaved(tenant, cmd.savedId, cmd.amount)
		} else {
			throw new RuntimeException(cmd.type)
		}
	}
	
	private void payCard(String tenant, long number, long cvv, long expMonth, long expYear, BigDecimal amount) {
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
	}
	
	private void payBank(String tenant, long routing, long account, BigDecimal amount) {
		BankAccount bankAccount = bankService.lookupAccount(tenant, routing, account, true)
		
		if (!bankAccount.bankRouting.present) {
			throw new InvalidRoutingException()
		}
		if (!bankAccount.present) {
			throw new InvalidAccountException()
		}
		
		bankAccount.balance += amount
		bankAccount.save(flush: true)
	}
	
	private void paySaved(String tenant, UUID savedId, BigDecimal amount) {
		Saved saved = Saved.findByTenantAndSavedId(tenant, savedId)
		
		if (saved.type == "card") {
			payCard(tenant, saved.cardNumber, saved.cardCvv, saved.cardExpMonth, saved.cardExpYear, amount)
		} else if (saved.type == "bank") {
			payBank(tenant, saved.bankRouting, saved.bankAccount, amount)
		} else {
			throw new RuntimeException(saved.type)
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