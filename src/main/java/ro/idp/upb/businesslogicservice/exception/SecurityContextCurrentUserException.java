/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.exception;

public class SecurityContextCurrentUserException extends RuntimeException {
	public SecurityContextCurrentUserException() {
		super();
	}

	@Override
	public String getMessage() {
		return "Could not get current user from Security Context";
	}
}
