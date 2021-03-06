package pay

class UrlMappings {
	static mappings = {
		"/"(view: "/home")
		"500"(view: "/serverError/internalServerError")
		"400"(view: "/clientError/badRequest")
		"404"(view: "/clientError/notFound")
		"402"(view: "/clientError/payment")
		
		get "/card/$cardNumber"(controller: "card", action: "view")
		put "/card/$cardNumber"(controller: "card", action: "update")
		
		get "/bank/$routingNumber/$accountNumber"(controller: "bank", action: "view")
		put "/bank/$routingNumber"(controller: "bank", action: "updateRouting")
		put "/bank/$routingNumber/$accountNumber"(controller: "bank", action: "updateAccount")
		post "/bank/$routingNumber/$accountNumber/$chargeId"(controller: "bank", action: "settle")
		
		get "/settlement/$chargeId"(controller: "settlement", action: "view")
		
		post "/pay"(controller: "pay", action: "pay")
		post "/save"(controller: "saved", action: "save")
	}
}
