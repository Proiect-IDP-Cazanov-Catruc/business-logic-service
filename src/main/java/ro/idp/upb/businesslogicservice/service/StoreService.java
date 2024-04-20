/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.idp.upb.businesslogicservice.config.SecurityUtils;
import ro.idp.upb.businesslogicservice.data.dto.request.OrderPostDto;
import ro.idp.upb.businesslogicservice.data.dto.response.CategoryGetDto;
import ro.idp.upb.businesslogicservice.data.dto.response.GetOrderDto;
import ro.idp.upb.businesslogicservice.data.dto.response.ProductGetDto;
import ro.idp.upb.businesslogicservice.data.entity.User;
import ro.idp.upb.businesslogicservice.data.enums.Role;
import ro.idp.upb.businesslogicservice.exception.handle.RestTemplateResponseErrorHandler;
import ro.idp.upb.businesslogicservice.utils.StaticConstants;
import ro.idp.upb.businesslogicservice.utils.UrlBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreService {
	private final StaticConstants staticConstants;
	private final ObjectMapper objectMapper;

	public List<CategoryGetDto> getAllCategories() {
		log.info("Fetching all store categories...");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(
				new RestTemplateResponseErrorHandler(
						objectMapper, () -> log.error("Unable to fetch all categories")));
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("categories-endpoint", staticConstants.ioCategoriesEndpoint);

		String url =
				UrlBuilder.replacePlaceholdersInString("${io-service-url}${categories-endpoint}", params);

		ResponseEntity<CategoryGetDto[]> response;

		response = restTemplate.exchange(url, HttpMethod.GET, entity, CategoryGetDto[].class);

		log.info(
				"Successfully fetched all categories. Total: {}!",
				response.getBody() == null ? 0 : response.getBody().length);
		return List.of(response.getBody());
	}

	public List<ProductGetDto> getProducts(UUID categoryId) {
		log.info("Fetching store products, with optional categoryId {}...", categoryId);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(
				new RestTemplateResponseErrorHandler(
						objectMapper,
						() -> log.error("Unable to fetch products, with categoryId {}", categoryId)));
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("products-endpoint", staticConstants.ioProductsEndpoint);

		String url =
				UrlBuilder.replacePlaceholdersInString("${io-service-url}${products-endpoint}", params);

		String urlEncoded =
				UriComponentsBuilder.fromHttpUrl(url)
						.queryParam("categoryId", categoryId)
						.encode()
						.toUriString();

		ResponseEntity<ProductGetDto[]> response;
		response = restTemplate.exchange(urlEncoded, HttpMethod.GET, entity, ProductGetDto[].class);

		log.info(
				"Successfully fetched all products, with categoryId {}. Total: {}!",
				categoryId,
				response.getBody() == null ? 0 : response.getBody().length);
		return List.of(response.getBody());
	}

	public GetOrderDto placeOrder(OrderPostDto dto) {
		User currentUser = SecurityUtils.getCurrentUser();
		log.info(
				"Placing order for user {} and {} products!",
				currentUser.getId(),
				dto.getProductsIds().size());
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(
				new RestTemplateResponseErrorHandler(
						objectMapper,
						() -> log.error("Unable to place order for user {}", currentUser.getId())));
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("orders-endpoint", staticConstants.ioOrdersEndpoint);

		String url =
				UrlBuilder.replacePlaceholdersInString("${io-service-url}${orders-endpoint}", params);

		dto.setUserId(currentUser.getId());
		ResponseEntity<GetOrderDto> response;
		HttpEntity<?> entity = new HttpEntity<>(dto, headers);
		response = restTemplate.postForEntity(url, entity, GetOrderDto.class);
		log.info(
				"Successfully placed order for user {}. Order id: {}!",
				currentUser.getId(),
				response.getBody() == null ? null : response.getBody().getOrderId());
		return response.getBody();
	}

	public List<GetOrderDto> getOrders(final UUID byUserId, Boolean ownOrders) {
		User currentUser = SecurityUtils.getCurrentUser();
		log.info(
				"Getting orders, byUserId {}, current user id {}, current user role {}, ownOrders {}...",
				byUserId,
				currentUser.getId(),
				currentUser.getRole(),
				ownOrders);
		UUID actualByUserId;
		if (currentUser.getRole().equals(Role.USER) || ownOrders) {
			actualByUserId = currentUser.getId();
		} else {
			actualByUserId = byUserId;
		}
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(
				new RestTemplateResponseErrorHandler(
						objectMapper,
						() ->
								log.error(
										"Unable to get orders, byUserId {}, current user id {}, current user role {}, ownOrders {}...",
										byUserId,
										currentUser.getId(),
										currentUser.getRole(),
										ownOrders)));
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("orders-endpoint", staticConstants.ioOrdersEndpoint);

		String url =
				UrlBuilder.replacePlaceholdersInString("${io-service-url}${orders-endpoint}", params);

		String urlEncoded =
				UriComponentsBuilder.fromHttpUrl(url)
						.queryParam("byUserId", actualByUserId)
						.encode()
						.toUriString();

		ResponseEntity<GetOrderDto[]> response;
		HttpEntity<?> entity = new HttpEntity<>(headers);
		response = restTemplate.exchange(urlEncoded, HttpMethod.GET, entity, GetOrderDto[].class);

		log.info(
				"Successfully got orders, byUserId {}, current user id {}, current user role {}, ownOrders {}! Total: {}!",
				byUserId,
				currentUser.getId(),
				currentUser.getRole(),
				ownOrders,
				response.getBody() == null ? null : response.getBody().length);
		return List.of(response.getBody());
	}
}
