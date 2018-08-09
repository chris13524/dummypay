package pay

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class BankRouting {
	static final long EXPIRE_AFTER = 60 * 60
	
	long created = System.currentTimeSeconds()
	
	static hasMany = [bankAccounts: BankAccount]
	
	String tenant
	
	long routing
	boolean present = true
	
	static constraints = {
		created nullable: false
		
		tenant nullable: false
		
		routing nullable: false
		present nullable: false
	}
}