package pay

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.json.JsonOutput

@GrailsCompileStatic
@Transactional
class SavedService {
	String save(String tenant, SaveCommand cmd) {
		if (cmd.type == "card") {
			return JsonOutput.toJson([
					t: "card",
					n: cmd.cardNumber,
					c: cmd.cardCvv,
					m: cmd.cardExpMonth,
					y: cmd.cardExpYear
			])
		} else if (cmd.type == "bank") {
			return JsonOutput.toJson([
					t: "bank",
					r: cmd.bankRouting,
					a: cmd.bankAccount
			])
		} else {
			throw new RuntimeException(cmd.type)
		}
	}
}