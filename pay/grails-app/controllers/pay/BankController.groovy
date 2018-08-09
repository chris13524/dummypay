package pay

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class BankController implements Validation, Tenant, ExceptionHandlers {
	BankService bankService
	
	def view() {
		BankAccount bankAccount = bankService.lookupAccount(tenant(), params.routingNumber as long, params.accountNumber as long)
		
		response.status = 200
		return [bankAccount: bankAccount]
	}
	
	def updateRouting(RoutingUpdateCommand cmd) {
		validate(cmd)
		
		BankRouting.withTransaction {
			BankRouting bankRouting = bankService.lookupRouting(tenant(), params.routingNumber as long, true)
			bankService.updateRouting(bankRouting, cmd)
			bankRouting.save(flush: true)
		}
		
		response.status = 204
	}
	
	def updateAccount(AccountUpdateCommand cmd) {
		validate(cmd)
		
		BankAccount.withTransaction {
			BankAccount bankAccount = bankService.lookupAccount(tenant(), params.routingNumber as long, params.accountNumber as long, true)
			bankService.updateAccount(bankAccount, cmd)
			bankAccount.save(flush: true)
		}
		
		response.status = 204
	}
}

@GrailsCompileStatic
class RoutingUpdateCommand implements Validateable {
	Boolean exists
	
	static constraints = {
		exists nullable: true
	}
}

@GrailsCompileStatic
class AccountUpdateCommand implements Validateable {
	Boolean exists
	BigDecimal balance
	
	static constraints = {
		exists nullable: true
		balance nullable: true
	}
}