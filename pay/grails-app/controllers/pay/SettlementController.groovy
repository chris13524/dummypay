package pay

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class SettlementController implements Validation, Tenant, ExceptionHandlers {
	def view() {
		BankCharge.withTransaction {
			BankCharge charge = BankCharge.findByChargeId(params.chargeId as String)
			if (charge == null) throw new NotFoundException()
			
			return [bankCharge: charge]
		}
	}
}
