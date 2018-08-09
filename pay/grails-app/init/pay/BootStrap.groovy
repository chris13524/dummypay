package pay

class BootStrap {
	ExpirationService expirationService
	
	def init = { servletContext ->
		expirationService.init()
	}
	
	def destroy = {
		expirationService.destroy()
	}
}
