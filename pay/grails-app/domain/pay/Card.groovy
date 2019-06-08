package pay

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Card {
	static final long EXPIRE_AFTER = 60 * 60
	
	long created = System.currentTimeSeconds()
	
	String tenant
	
	long number
	boolean present = true
	
	Long cvv = null
	Long expMonth = null
	Long expYear = null
	
	BigDecimal balance = 0
	
	static constraints = {
		created nullable: false
		
		tenant nullable: false
		
		number nullable: false
		present nullable: false
		
		cvv nullable: true
		expMonth nullable: true
		expYear nullable: true
		
		balance nullable: false
	}
}
