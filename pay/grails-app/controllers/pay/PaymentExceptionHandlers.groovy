package pay

trait PaymentExceptionHandlers {
	def handleInvalidCardException(InvalidCardException e) {
		response.sendError(402, "invalidCard")
	}
	
	def handleInvalidRoutingException(InvalidRoutingException e) {
		response.sendError(402, "invalidRouting")
	}
	
	def handleInvalidAccountException(InvalidAccountException e) {
		response.sendError(402, "invalidAccount")
	}
	
	def handleExpiredException(ExpiredException e) {
		response.sendError(402, "expired")
	}
	
	def handleDeclinedException(DeclinedException e) {
		response.sendError(402, "declined")
	}
}