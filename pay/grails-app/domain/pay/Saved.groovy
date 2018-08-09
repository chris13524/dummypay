package pay

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Saved {
	static final long EXPIRE_AFTER = 60 * 60
	
	long created = System.currentTimeSeconds()
	
	String tenant
	UUID savedId = UUID.randomUUID()
	
	String type
	
	Long cardNumber
	Long cardCvv
	Long cardExpMonth
	Long cardExpYear
	
	Long bankRouting
	Long bankAccount
	
	static constraints = {
		created nullable: false
		
		tenant nullable: false
		savedId type: "uuid-char", length: 36
		
		type nullable: false, inList: ["card", "bank"]
		
		def required = { String type -> { Object property, Saved saved -> saved.type != type || property != null } }
		
		cardNumber nullable: true, validator: required("card")
		cardCvv nullable: true, validator: required("card")
		cardExpMonth nullable: true, validator: required("card")
		cardExpYear nullable: true, validator: required("card")
		
		bankRouting nullable: true, validator: required("bank")
		bankAccount nullable: true, validator: required("bank")
	}
}