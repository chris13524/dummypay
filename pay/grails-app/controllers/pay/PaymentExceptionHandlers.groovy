package pay

trait PaymentExceptionHandlers {
	def handleInvalidCardException(InvalidCardException e) {
		response.sendError(470, "invalidCard")
	}
	
	def handleInvalidRoutingException(InvalidRoutingException e) {
		response.sendError(470, "invalidRouting")
	}
	
	def handleInvalidAccountException(InvalidAccountException e) {
		response.sendError(470, "invalidAccount")
	}
	
	def handleExpiredException(ExpiredException e) {
		response.sendError(470, "expired")
	}
	
	def handleDeclinedException(DeclinedException e) {
		response.sendError(470, "declined")
	}
}