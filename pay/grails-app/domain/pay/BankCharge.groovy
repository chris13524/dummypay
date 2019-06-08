package pay

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class BankCharge {
	String chargeId = UUID.randomUUID().toString()
	String settleStatus = null
	Long settledOn = null
	
	static belongsTo = [bankAccount: BankAccount]
	
	static constraints = {
		chargeId nullable: false, blank: false
		settleStatus nullable: true
		settledOn nullable: true
	}
}
