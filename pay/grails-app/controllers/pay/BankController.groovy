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
	
	def settle(SettleCommand cmd) {
		validate(cmd)
		
		if (cmd.settleOn == null) cmd.settleOn = System.currentTimeSeconds()
		
		BankCharge.withTransaction {
			BankCharge charge = BankCharge.findByChargeId(params.chargeId as String)
			if (charge == null) throw new NotFoundException("chargeId")
			if (charge.bankAccount.account != params.accountNumber as long) throw new NotFoundException("account")
			if (charge.bankAccount.bankRouting.routing != params.routingNumber as long) throw new NotFoundException("routing")
			
			charge.settledOn = cmd.settleOn
			
			if (!charge.bankAccount.bankRouting.present) charge.settleStatus = "invalidRouting"
			else if (!charge.bankAccount.present) charge.settleStatus = "invalidAccount"
			else charge.settleStatus = "success"
			
			charge.save(flush: true)
		}
		
		response.status = 204
	}
}

@GrailsCompileStatic
class RoutingUpdateCommand implements Validateable {
	Boolean exists
	Boolean deferErrors
	
	static constraints = {
		exists nullable: true
		deferErrors nullable: true
	}
}

@GrailsCompileStatic
class AccountUpdateCommand implements Validateable {
	Boolean exists
	Boolean deferErrors
	BigDecimal balance
	
	static constraints = {
		exists nullable: true
		deferErrors nullable: true
		balance nullable: true
	}
}

@GrailsCompileStatic
class SettleCommand implements Validateable {
	Long settleOn
	
	static constraints = {
		settleOn nullable: true
	}
}
