/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.config;

import java.util.Optional;
import java.util.stream.Stream;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ro.idp.upb.businesslogicservice.data.entity.User;

public final class SecurityUtils {

	public static Optional<String> getCurrentUserLogin() {
		var securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication())
				.map(
						authentication -> {
							if (authentication.getPrincipal() instanceof final UserDetails springSecurityUser) {
								return springSecurityUser.getUsername();
							}
							if (authentication.getPrincipal() instanceof final String authenticationPrincipal) {
								return authenticationPrincipal;
							}
							return null;
						});
	}

	public static User getCurrentUser() throws AuthenticationException {
		var securityContext = SecurityContextHolder.getContext();
		User user =
				Optional.ofNullable(securityContext.getAuthentication())
						.map(
								authentication -> {
									if (authentication.getPrincipal() instanceof final User principalUser) {
										return principalUser;
									}
									return null;
								})
						.orElseThrow(
								() -> new AuthenticationException("Could not get current authenticated user!"));
		return user;
	}

	public static boolean isAuthenticated() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated();
	}

	public static boolean isCurrentUserInRole(final String role) {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null
				&& getAuthorities(authentication).anyMatch(role::equalsIgnoreCase);
	}

	private static Stream<String> getAuthorities(final Authentication authentication) {
		return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
	}
}
