package pay

import grails.validation.Validateable

trait Validation {
	static boolean validate(Validateable cmd) {
		if (cmd.hasErrors()) {
			throw new BadRequestException(cmd.errors.allErrors[0].toString())
		}
	}
}