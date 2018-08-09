package pay

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class CardController implements Validation, Tenant, ExceptionHandlers {
	CardService cardService
	
	def view() {
		Card card = cardService.lookupCard(tenant(), params.cardNumber as long)
		
		response.status = 200
		return [card: card]
	}
	
	def update(CardUpdateCommand cmd) {
		validate(cmd)
		
		Card.withTransaction {
			Card card = cardService.lookupCard(tenant(), params.cardNumber as long, true)
			cardService.updateCard(card, cmd)
			card.save(flush: true)
		}
		
		response.status = 204
	}
}

@GrailsCompileStatic
class CardUpdateCommand implements Validateable {
	Boolean exists
	Long cvv
	Long expMonth
	Long expYear
	BigDecimal balance
	
	static constraints = {
		exists nullable: true
		cvv nullable: true
		expMonth nullable: true
		expYear nullable: true
		balance nullable: true
	}
}