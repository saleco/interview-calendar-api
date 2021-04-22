package com.github.saleco.interview.calendar.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception is used to tell the caller, that the request has invalid or missing fields.
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BusinessException {

	/**
	 * Exception, that is to be thrown, when the resource is not found
	 * This leads to a return code of 404 (Not Found) for the requesting system.
	 *
	 * @param errorMessage description validation problems
	 * @param cause        The original validation exception
	 */
	public NotFoundException(String errorMessage) {
		super("VAL", errorMessage);
	}
}
