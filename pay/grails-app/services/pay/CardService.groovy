package pay

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@GrailsCompileStatic
@Transactional
class CardService {
	Card lookupCard(String tenant, long number, boolean lock = false) {
		Card card = Card.findByTenantAndNumber(tenant, number, [lock: lock])
		if (card == null) {
			card = new Card(tenant: tenant, number: number)
			card.save(flush: true)
		}
		
		return card
	}
	
	void updateCard(Card card, CardUpdateCommand cmd) {
		if (cmd.exists != null) card.present = cmd.exists
		if (cmd.cvv != null) card.cvv = cmd.cvv
		if (cmd.expMonth != null) card.expMonth = cmd.expMonth
		if (cmd.expYear != null) card.expYear = cmd.expYear
		if (cmd.balance != null) card.balance = cmd.balance
	}
}