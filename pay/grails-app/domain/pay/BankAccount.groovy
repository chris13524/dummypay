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
	
	BigDecimal balance = 0
	
	static constraints = {
		created nullable: false
		
		tenant nullable: false
		
		account nullable: false
		present nullable: false
		
		balance nullable: false
	}
}