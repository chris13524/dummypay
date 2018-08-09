package pay

import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional

@GrailsCompileStatic
@Transactional
class BankService {
	BankRouting lookupRouting(String tenant, long routing, boolean lock = false) {
		BankRouting bankRouting = BankRouting.findByTenantAndRouting(tenant, routing, [lock: lock])
		if (bankRouting == null) {
			bankRouting = new BankRouting(tenant: tenant, routing: routing)
			bankRouting.save(flush: true)
		}
		
		return bankRouting
	}
	
	void updateRouting(BankRouting bankRouting, RoutingUpdateCommand cmd) {
		if (cmd.exists != null) bankRouting.present = cmd.exists
	}
	
	BankAccount lookupAccount(String tenant, long routing, long account, boolean lock = false) {
		BankRouting bankRouting = lookupRouting(tenant, routing, lock)
		
		BankAccount bankAccount = BankAccount.findByTenantAndAccount(tenant, account, [lock: lock])
		if (bankAccount == null) {
			bankAccount = new BankAccount(tenant: tenant, account: account)
			bankRouting.addToBankAccounts(bankAccount)
			bankRouting.save(flush: true)
		}
		
		assert bankRouting == bankAccount.bankRouting
		
		return bankAccount
	}
	
	void updateAccount(BankAccount bankAccount, AccountUpdateCommand cmd) {
		if (cmd.exists != null) bankAccount.present = cmd.exists
		if (cmd.balance != null) bankAccount.balance = cmd.balance
	}
}