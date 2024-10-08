/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.idp.upb.businesslogicservice.data.dto.request.PostManagerDto;
import ro.idp.upb.businesslogicservice.data.dto.response.UserDto;
import ro.idp.upb.businesslogicservice.data.entity.User;
import ro.idp.upb.businesslogicservice.exception.handle.RestTemplateResponseErrorHandler;
import ro.idp.upb.businesslogicservice.utils.StaticConstants;
import ro.idp.upb.businesslogicservice.utils.UrlBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final StaticConstants staticConstants;
	private final ObjectMapper objectMapper;

	public Optional<User> findByEmail(String email) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(
				new RestTemplateResponseErrorHandler(
						objectMapper, () -> log.error("Unable to find user details by user email {}!", email)));
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("users-endpoint", staticConstants.ioUsersEndpoint);
		params.put("find-by-email-endpoint", staticConstants.ioFindByEmailEndpoint);
		params.put("email", email);

		String url =
				UrlBuilder.replacePlaceholdersInString(
						"${io-service-url}${users-endpoint}${find-by-email-endpoint}/${email}", params);

		log.info("Find user details by user email {} request to IO SERVICE!", email);

		String urlTemplate = UriComponentsBuilder.fromHttpUrl(url).encode().toUriString();

		ResponseEntity<UserDto> response;

		response = restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, UserDto.class);

		log.info("Successfully fetched user details by user email {}!", email);
		UserDto dtoResponse = response.getBody();
		return Optional.of(userDtoToEntity(dtoResponse));
	}

	public User userDtoToEntity(UserDto userDto) {
		return User.builder()
				.id(userDto.getId())
				.firstname(userDto.getFirstName())
				.lastname(userDto.getLastName())
				.email(userDto.getEmail())
				.role(userDto.getRole())
				.build();
	}

	@PreAuthorize("hasRole('ADMIN')")
	public UserDto createManager(PostManagerDto dto) {
		log.info(
				"Creating manager [Firstname: {}], [Lastname: {}], [Email: {}]...",
				dto.getFirstName(),
				dto.getLastName(),
				dto.getEmail());

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(
				new RestTemplateResponseErrorHandler(
						objectMapper,
						() ->
								log.error(
										"Unable to create manager [Firstname: {}], [Lastname: {}], [Email: {}]!",
										dto.getFirstName(),
										dto.getLastName(),
										dto.getEmail())));
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(dto, headers);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("users-endpoint", staticConstants.ioUsersEndpoint);
		params.put("post-manager-endpoint", staticConstants.ioPostManagerEndpoint);

		String url =
				UrlBuilder.replacePlaceholdersInString(
						"${io-service-url}${users-endpoint}${post-manager-endpoint}", params);

		ResponseEntity<UserDto> response = restTemplate.postForEntity(url, entity, UserDto.class);

		UserDto responseDto = response.getBody();

		log.info(
				"Created manager [Firstname: {}], [Lastname: {}], [Email: {}], associated id: {}!",
				dto.getFirstName(),
				dto.getLastName(),
				dto.getEmail(),
				responseDto.getId());
		return responseDto;
	}
}
