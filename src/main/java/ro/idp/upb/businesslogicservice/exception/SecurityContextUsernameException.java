/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.exception;

public class SecurityContextUsernameException extends RuntimeException {
	public SecurityContextUsernameException() {
		super();
	}

	@Override
	public String getMessage() {
		return "Could not get username from Security Context";
	}
}
