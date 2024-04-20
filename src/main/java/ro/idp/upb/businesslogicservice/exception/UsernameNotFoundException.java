/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.exception;

import java.util.Optional;
import java.util.UUID;

public class UsernameNotFoundException extends RuntimeException {
	private final Optional<String> email;
	private final Optional<UUID> userId;

	public UsernameNotFoundException(String email) {
		super();
		this.email = Optional.ofNullable(email);
		this.userId = Optional.empty();
	}

	public UsernameNotFoundException(UUID userId) {
		super();
		this.email = Optional.empty();
		this.userId = Optional.ofNullable(userId);
	}

	@Override
	public String getMessage() {
		if (email.isPresent() && userId.isPresent()) {
			return String.format("User with email %s and id %s not found!", email.get(), userId.get());
		} else if (email.isEmpty() && userId.isPresent()) {
			return String.format("User with id %s not found!", userId.get());
		} else if (email.isPresent()) {
			return String.format("User with email %s not found!", email.get());
		}
		return "User not found!";
	}
}
