package pay

trait Tenant {
	String tenant() {
		return request.getHeader("Authorization") ?: "null"
	}
}