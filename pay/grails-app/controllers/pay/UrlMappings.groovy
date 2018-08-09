package pay

class UrlMappings {
	static mappings = {
		"/"(view: "/home")
		"500"(view: "/serverError/internalServerError")
		"404"(view: "/clientError/notFound")
		
		get "/card/$cardNumber"(controller: "card", action: "view")
		put "/card/$cardNumber"(controller: "card", action: "update")
		
		get "/bank/$routingNumber/$accountNumber"(controller: "bank", action: "view")
		put "/bank/$routingNumber"(controller: "bank", action: "updateRouting")
		put "/bank/$routingNumber/$accountNumber"(controller: "bank", action: "updateAccount")
		
		post "/pay"(controller: "pay", action: "pay")
		post "/save"(controller: "saved", action: "save")
	}
}
