package pay

import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

@GrailsCompileStatic
class PayController implements Validation, Tenant, ExceptionHandlers, PaymentExceptionHandlers {
	PayService payService
	
	def pay(PayCommand cmd) {
		validate(cmd)
		
		String chargeId = payService.pay(tenant(), cmd)
		
		return [chargeId: chargeId]
	}
}

@GrailsCompileStatic
class PayCommand implements Validateable {
	String type
	
	Long cardNumber
	Long cardCvv
	Long cardExpMonth
	Long cardExpYear
	
	Long bankRouting
	Long bankAccount
	
	String savedId
	
	BigDecimal amount
	
	static constraints = {
		type nullable: false, inList: ["card", "bank", "saved"]
		
		def required = { String type -> { Object property, PayCommand cmd -> cmd.type != type || property != null } }
		
		cardNumber nullable: true, validator: required("card")
		cardCvv nullable: true, validator: required("card")
		cardExpMonth nullable: true, validator: required("card")
		cardExpYear nullable: true, validator: required("card")
		
		bankRouting nullable: true, validator: required("bank")
		bankAccount nullable: true, validator: required("bank")
		
		savedId nullable: true, validator: required("saved")
		
		amount nullable: false
	}
}
