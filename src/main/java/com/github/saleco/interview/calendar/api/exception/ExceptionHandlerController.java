package com.github.saleco.interview.calendar.api.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.saleco.interview.calendar.api.utils.InterviewCalendarAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.UndeclaredThrowableException;

@Slf4j
@ControllerAdvice("com.github.saleco.interview.calendar.api")
public class ExceptionHandlerController {


	private static final String START_TAG = "{";

	private static final String END_TAG = "}";
	public static final String THERE_IS_A_VALIDATION_RULE_THAT_PREVENTS_THE_REQUEST_SEE_RULE = "There is a validation rule that prevents the request. See rule | ";
	public static final String NOT_NULL = "NotNull";
	public static final String SIZE = "Size";
	public static final String VALIDATION_EXCEPTION_MESSAGE = "ValidationException: %s";
	public static final String MANDATORY = ",mandatory";

	@ExceptionHandler(value = { ConstraintViolationException.class, MethodArgumentNotValidException.class,
			BusinessException.class, Exception.class })
	protected ResponseEntity<Object> handleBusinessException(WebRequest request, Exception ex) {
		if (ex instanceof ConstraintViolationException) {
			ex = handleContraintViolationException(ex);
		}

		if (ex instanceof MethodArgumentNotValidException) {
			ex = handleMethodArgumentNotValidException(ex);
		}

		if (ex instanceof BindException) {
			ex = handleBindException(ex);
		}

		if (ex instanceof InterviewCalendarAPIException) {
			return handleInterviewCalendarAPIException(ex);
		}

		if(ex instanceof UndeclaredThrowableException && ((UndeclaredThrowableException) ex).getUndeclaredThrowable().getCause() instanceof InterviewCalendarAPIException){
			return handleInterviewCalendarAPIExceptionUndeclared((UndeclaredThrowableException) ex);
		}

		if(ex instanceof HttpMessageNotReadableException && ex.getCause() instanceof InvalidFormatException) {
			return handleInvalidFormatException(ex);
		}

		log.error("Unknown Exception detected in ExceptionHandlerController", ex);

		InterviewCalendarAPIResponse response = new InterviewCalendarAPIResponse(HttpStatus.BAD_REQUEST.value(), "We are sorry, something unexpected happened. Please try it again later.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(response);
	}

	private ResponseEntity<Object> handleInvalidFormatException(Exception ex) {
		InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
		String message = invalidFormatException.getMessage();
		InterviewCalendarAPIResponse response = new InterviewCalendarAPIResponse(HttpStatus.BAD_REQUEST.value(), message);
		log.error(message);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response);
	}

	private ResponseEntity<Object> handleInterviewCalendarAPIExceptionUndeclared(UndeclaredThrowableException ex) {
		InterviewCalendarAPIException exception = (InterviewCalendarAPIException) ex.getUndeclaredThrowable().getCause();
		HttpStatus status = exception.getClass().getAnnotation(ResponseStatus.class).value();
		String message = exception.getMessage();
		InterviewCalendarAPIResponse response = new InterviewCalendarAPIResponse(status.value(), message);
		log.error(message);
		return ResponseEntity.status(status).body(response);
	}

	private ResponseEntity<Object> handleInterviewCalendarAPIException(Exception ex) {
		HttpStatus status = ex.getClass().getAnnotation(ResponseStatus.class).value();
		String message = ((InterviewCalendarAPIException) ex).getMessage();
		InterviewCalendarAPIResponse response = new InterviewCalendarAPIResponse(status.value(), message);
		log.error(message);
		return ResponseEntity.status(status).body(response);
	}

	private Exception handleBindException(Exception ex) {
		StringBuilder message = new StringBuilder(THERE_IS_A_VALIDATION_RULE_THAT_PREVENTS_THE_REQUEST_SEE_RULE);
		BindException nve = (BindException) ex;
		BindingResult bindingResult = nve.getBindingResult();
		for (ObjectError error : bindingResult.getAllErrors()) {
			String code = error.getCode();
			String f = error.getCodes()[1];
			String field = f.substring(f.indexOf('.') + 1);

			if (NOT_NULL.equals(code)) {
				message.append(START_TAG + field + MANDATORY + END_TAG);
			}
			if ("Size".equals(code)) {
				String min = "" + error.getArguments()[2];
				String max = "" + error.getArguments()[1];
				message.append(START_TAG + field + ",size," + min + "-" + max + END_TAG);
			}
		}

		log.info(VALIDATION_EXCEPTION_MESSAGE, message, ex);
		ex = new ValidationException(message.toString());
		return ex;
	}

	private Exception handleMethodArgumentNotValidException(Exception ex) {
		StringBuilder message = new StringBuilder(THERE_IS_A_VALIDATION_RULE_THAT_PREVENTS_THE_REQUEST_SEE_RULE);
		MethodArgumentNotValidException nve = (MethodArgumentNotValidException) ex;
		BindingResult bindingResult = nve.getBindingResult();
		for (ObjectError error : bindingResult.getAllErrors()) {
			String code = error.getCode();
			String f = error.getCodes()[1];
			String field = f.substring(f.indexOf('.') + 1);

			if (NOT_NULL.equals(code)) {
				message.append(START_TAG + field + ",mandatory" + END_TAG);
			}
			if ("Size".equals(code)) {
				String min = "" + error.getArguments()[2];
				String max = "" + error.getArguments()[1];
				message.append(START_TAG + field + ",size," + min + "-" + max + END_TAG);
			}
		}

		log.info("ValidationException: {}", message);
		ex = new ValidationException(message.toString());
		return ex;
	}

	private Exception handleContraintViolationException(Exception ex) {
		StringBuilder message = new StringBuilder(THERE_IS_A_VALIDATION_RULE_THAT_PREVENTS_THE_REQUEST_SEE_RULE);
		ConstraintViolationException cve = (ConstraintViolationException) ex;
		for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
			ConstraintDescriptor<? extends Annotation> descriptor = violation.getConstraintDescriptor();
			Annotation annotation = descriptor.getAnnotation();
			String annotationName = annotation.annotationType().getCanonicalName();
			if (annotationName != null) {
				if (annotationName.endsWith(NOT_NULL)) {
					message.append(START_TAG + violation.getPropertyPath() + ",mandatory" + END_TAG);
				}
				if (annotationName.endsWith(SIZE)) {
					String min = "" + descriptor.getAttributes().get("min");
					String max = "" + descriptor.getAttributes().get("max");
					message.append(START_TAG + violation.getPropertyPath() + ",size," + min + "-" + max + END_TAG);
				}
			}
		}

		log.info("ValidationException: {}", message);
		ex = new ValidationException(message.toString());
		return ex;
	}
}
