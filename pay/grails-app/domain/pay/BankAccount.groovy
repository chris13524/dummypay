package pay

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class BankAccount {
	static final long EXPIRE_AFTER = 60 * 60
	
	long created = System.currentTimeSeconds()
	
	static belongsTo = [bankRouting: BankRouting]
	
	String tenant
	
	long account
	boolean present = true
	boolean deferErrors = false
	
	BigDecimal balance = 0
	
	static hasMany = [
			charges: BankCharge
	]
	
	static constraints = {
		created nullable: false
		
		tenant nullable: false
		
		account nullable: false
		present nullable: false
		deferErrors nullable: true
		
		balance nullable: false
	}
}
