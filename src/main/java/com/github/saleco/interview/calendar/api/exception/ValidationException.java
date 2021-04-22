package com.github.saleco.interview.calendar.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception is used to tell the caller, that the request has invalid or missing fields.
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BusinessException {

	/**
	 * Exception, that is to be thrown, when the validation of parameters shows missing parameters, wrong sizes or
	 * illegal patterns. This leads to a return code of 400 (Bad Message) for the requesting system.
	 *
	 * @param errorMessage description validation problems
	 * @param cause        The original validation exception
	 */
	public ValidationException(String errorMessage) {
		super("VAL", errorMessage);
	}
}
