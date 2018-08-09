package pay

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class SavedController implements Validation, Tenant, ExceptionHandlers {
	SavedService savedService
	
	def save(SaveCommand cmd) {
		validate(cmd)
		
		UUID savedId = savedService.save(tenant(), cmd)
		
		response.status = 200
		return [savedId: savedId]
	}
}

@GrailsCompileStatic
class SaveCommand implements Validateable {
	String type
	
	Long cardNumber
	Long cardCvv
	Long cardExpMonth
	Long cardExpYear
	
	Long bankRouting
	Long bankAccount
	
	static constraints = {
		type nullable: false, inList: ["card", "bank"]
		
		def required = { String type -> { Object property, SaveCommand cmd -> cmd.type != type || property != null } }
		
		cardNumber nullable: true, validator: required("card")
		cardCvv nullable: true, validator: required("card")
		cardExpMonth nullable: true, validator: required("card")
		cardExpYear nullable: true, validator: required("card")
		
		bankRouting nullable: true, validator: required("bank")
		bankAccount nullable: true, validator: required("bank")
	}
}