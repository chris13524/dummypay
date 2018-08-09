package pay

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@GrailsCompileStatic
@Transactional
class SavedService {
	UUID save(String tenant, SaveCommand cmd) {
		Saved saved = new Saved()
		
		saved.tenant = tenant
		
		if (cmd.type == "card") {
			saved.type = "card"
			saved.cardNumber = cmd.cardNumber
			saved.cardCvv = cmd.cardCvv
			saved.cardExpMonth = cmd.cardExpMonth
			saved.cardExpYear = cmd.cardExpYear
		} else if (cmd.type == "bank") {
			saved.type = "bank"
			saved.bankRouting = cmd.bankRouting
			saved.bankAccount = cmd.bankAccount
		} else {
			throw new RuntimeException(cmd.type)
		}
		
		saved.save(flush: true)
		
		return saved.savedId
	}
}