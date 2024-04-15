package ro.idp.upb.businesslogicservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.idp.upb.businesslogicservice.utils.StaticConstants;
import ro.idp.upb.businesslogicservice.utils.UrlBuilder;
import ro.idp.upb.businesslogicservice.data.dto.response.UserDto;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AuthInterrogator {
    private final StaticConstants staticConstants;

    public UserDto fetch(String token) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "Bearer " + token);

        Map<String, Object> params = new HashMap<>();
        params.put("auth-service-url", staticConstants.authServiceUrl);
        params.put("users-me-endpoint", staticConstants.authUsersMeEndpoint);

        final String authUsersMeEndpointUrl = UrlBuilder.replacePlaceholdersInString(
                "${auth-service-url}${users-me-endpoint}",
                params
        );

        final HttpEntity<Void> entity = new HttpEntity<>(headers);
        final ResponseEntity<UserDto> response = restTemplate.exchange(authUsersMeEndpointUrl, HttpMethod.GET, entity, UserDto.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AccessDeniedException("Invalid token");
        }

        return response.getBody();
    }
}
