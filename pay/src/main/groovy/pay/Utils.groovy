package pay

import grails.validation.Validateable

import javax.servlet.http.HttpServletResponse

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST


class Utils {
	/**
	 * Tests the given command object for error. If errors are found, a 400 error is sent to the response object and true is returned. False otherwise.
	 */
	static boolean testForError(Validateable cmd, HttpServletResponse response) {
		if (cmd.hasErrors()) {
			response.sendError(SC_BAD_REQUEST, cmd.errors.allErrors[0].toString())
			return true
		}
		
		return false
	}
}