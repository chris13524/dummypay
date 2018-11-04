package pay

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@GrailsCompileStatic
class ExpirationService {
	private ScheduledExecutorService scheduler
	
	void init() {
		scheduler = Executors.newScheduledThreadPool(1)
		scheduler.scheduleAtFixedRate({
			deleteExpired()
		}, 0, 5, TimeUnit.MINUTES)
	}
	
	void destroy() {
		scheduler.shutdown()
		scheduler.awaitTermination(1, TimeUnit.MINUTES)
	}
	
	@Transactional
	private void deleteExpired() {
		Card.findAllByCreatedLessThan(System.currentTimeSeconds() - Card.EXPIRE_AFTER, [lock: true])
				.forEach({ Card card ->
			card.delete(flush: true)
		})
		
		BankAccount.findAllByCreatedLessThan(System.currentTimeSeconds() - BankAccount.EXPIRE_AFTER, [lock: true])
				.forEach({ BankAccount bankAccount ->
			bankAccount.delete(flush: true)
		})
		BankRouting.findAllByCreatedLessThan(System.currentTimeSeconds() - BankRouting.EXPIRE_AFTER, [lock: true])
				.forEach({ BankRouting bankRouting ->
			bankRouting.delete(flush: true)
		})
	}
}