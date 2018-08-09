package pay

trait ExceptionHandlers {
	def handleNotFound(NotFoundException e) {
		response.sendError(404, e.message)
	}
	
	def handleBadRequest(BadRequestException e) {
		response.sendError(400, e.message)
	}
	
	def handleUnauthorized(UnauthorizedException e) {
		response.sendError(401, e.message)
	}
	
	def handleForbidden(ForbiddenException e) {
		response.sendError(403, e.message)
	}
	
	def handleConflict(ConflictException e) {
		response.sendError(409, e.message)
	}
	
	def handleTooManyRequests(TooManyRequestsException e) {
		response.sendError(429, e.message)
	}
}

class NotFoundException extends Exception {
	NotFoundException() { super("Not Found") }
	
	NotFoundException(String message) { super(message) }
}

class BadRequestException extends Exception {
	BadRequestException() { super("Bad Request") }
	
	BadRequestException(String message) { super(message) }
}

class UnauthorizedException extends Exception {
	UnauthorizedException() { super("Unauthorized") }
	
	UnauthorizedException(String message) { super(message) }
}

class ForbiddenException extends Exception {
	ForbiddenException() { super("Forbidden") }
	
	ForbiddenException(String message) { super(message) }
}

class ConflictException extends Exception {
	ConflictException() {}
	
	ConflictException(String message) { super(message) }
}

class TooManyRequestsException extends Exception {
	TooManyRequestsException() { super("Too Many Requests") }
	
	TooManyRequestsException(String message) { super(message) }
}