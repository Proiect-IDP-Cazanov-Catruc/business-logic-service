/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.exception.handle;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.idp.upb.businesslogicservice.exception.RestTemplateException;
import ro.idp.upb.businesslogicservice.exception.SecurityContextCurrentUserException;
import ro.idp.upb.businesslogicservice.exception.SecurityContextUsernameException;
import ro.idp.upb.businesslogicservice.exception.UsernameNotFoundException;

@ControllerAdvice
public class ExceptionHandling {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleUsernameNotFoundException(
			UsernameNotFoundException ex, HttpServletRequest request) {
		ErrorMessage errorMessage =
				buildErrorMessage(HttpStatus.NOT_FOUND, ErrorCode.E_100, ex, request);
		return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
	}

	@ExceptionHandler(SecurityContextCurrentUserException.class)
	public ResponseEntity<ErrorMessage> securityContextCurrentUserException(
			SecurityContextCurrentUserException ex, HttpServletRequest request) {
		ErrorMessage errorMessage =
				buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E_005, ex, request);
		return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
	}

	@ExceptionHandler(SecurityContextUsernameException.class)
	public ResponseEntity<ErrorMessage> securityContextUsernameException(
			SecurityContextUsernameException ex, HttpServletRequest request) {
		ErrorMessage errorMessage =
				buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E_003, ex, request);
		return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorMessage> handleValidationErrors(
			MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<ValidationError> validationErrors =
				ex.getBindingResult().getFieldErrors().stream()
						.map(
								fieldError ->
										ValidationError.builder()
												.field(fieldError.getField())
												.message(fieldError.getDefaultMessage())
												.build())
						.toList();

		ErrorMessage errorMessage =
				buildErrorMessage(HttpStatus.BAD_REQUEST, ErrorCode.E_201, ex, request);
		errorMessage.setValidationErrors(validationErrors);
		errorMessage.setDebugMessage(ex.getTypeMessageCode());

		return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> exception(Exception ex, HttpServletRequest request) {
		ErrorMessage errorMessage =
				buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E_001, ex, request);
		return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
	}

	private ErrorMessage buildErrorMessage(
			HttpStatus code, ErrorCode errorCode, Exception e, HttpServletRequest request) {
		ErrorMessage error = new ErrorMessage(code, errorCode, e);
		error.setPath(request.getRequestURI());
		return error;
	}

	@ExceptionHandler(RestTemplateException.class)
	public ResponseEntity<ErrorMessage> handleRestTemplateException(
			RestTemplateException e, HttpServletRequest request) {
		ErrorMessage errorMessage = e.getErrorMessage();
		errorMessage.setPath(request.getRequestURI());

		return ResponseEntity.status(HttpStatus.valueOf(errorMessage.getStatus())).body(errorMessage);
	}
}
