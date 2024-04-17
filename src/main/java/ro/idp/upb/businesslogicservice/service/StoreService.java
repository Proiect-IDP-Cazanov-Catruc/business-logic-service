/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ro.idp.upb.businesslogicservice.config.SecurityUtils;
import ro.idp.upb.businesslogicservice.data.dto.request.OrderPostDto;
import ro.idp.upb.businesslogicservice.data.dto.response.CategoryGetDto;
import ro.idp.upb.businesslogicservice.data.dto.response.GetOrderDto;
import ro.idp.upb.businesslogicservice.data.dto.response.ProductGetDto;
import ro.idp.upb.businesslogicservice.data.entity.User;
import ro.idp.upb.businesslogicservice.data.enums.Role;
import ro.idp.upb.businesslogicservice.utils.StaticConstants;
import ro.idp.upb.businesslogicservice.utils.UrlBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreService {
	private final StaticConstants staticConstants;

	public ResponseEntity<?> getAllCategories() {
		log.info("Fetching all store categories...");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(headers);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("categories-endpoint", staticConstants.ioCategoriesEndpoint);

		String url =
				UrlBuilder.replacePlaceholdersInString("${io-service-url}${categories-endpoint}", params);

		ResponseEntity<CategoryGetDto[]> response;

		try {
			response = restTemplate.exchange(url, HttpMethod.GET, entity, CategoryGetDto[].class);
		} catch (HttpStatusCodeException e) {
			log.error("Unable to fetch all categories");
			return new ResponseEntity<>(
					e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		}

		log.info(
				"Successfully fetched all categories. Total: {}!",
				response.getBody() == null ? 0 : response.getBody().length);
		return response;
	}

	public ResponseEntity<?> getProducts(UUID categoryId) {
		log.info("Fetching store products, with optional categoryId {}...", categoryId);
		RestTemplate restTemplate = new RestTemplate();
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

		try {
			response = restTemplate.exchange(urlEncoded, HttpMethod.GET, entity, ProductGetDto[].class);
		} catch (HttpStatusCodeException e) {
			log.error("Unable to fetch products, with categoryId {}", categoryId);
			return new ResponseEntity<>(
					e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		}

		log.info(
				"Successfully fetched all products, with categoryId {}. Total: {}!",
				categoryId,
				response.getBody() == null ? 0 : response.getBody().length);
		return response;
	}

	public ResponseEntity<?> placeOrder(OrderPostDto dto) throws AuthenticationException {
		User currentUser = SecurityUtils.getCurrentUser();
		if (currentUser != null) {
			log.info(
					"Placing order for user {} and {} products!",
					currentUser.getId(),
					dto.getProductsIds().size());
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

			Map<String, Object> params = new HashMap<>();
			params.put("io-service-url", staticConstants.ioServiceUrl);
			params.put("orders-endpoint", staticConstants.ioOrdersEndpoint);

			String url =
					UrlBuilder.replacePlaceholdersInString("${io-service-url}${orders-endpoint}", params);

			dto.setUserId(currentUser.getId());
			ResponseEntity<GetOrderDto> response;
			try {
				HttpEntity<?> entity = new HttpEntity<>(dto, headers);
				response = restTemplate.postForEntity(url, entity, GetOrderDto.class);
			} catch (HttpStatusCodeException e) {
				log.error("Unable to place order for user {}", currentUser.getId());
				return new ResponseEntity<>(
						e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
			}

			log.info(
					"Successfully placed order for user {}. Order id: {}!",
					currentUser.getId(),
					response.getBody() == null ? null : response.getBody().getOrderId());
			return response;
		}
		log.error("Could not get current user to place order!");
		return ResponseEntity.internalServerError().build();
	}

	public ResponseEntity<?> getOrders(UUID byUserId, Boolean ownOrders)
			throws AuthenticationException {
		User currentUser = SecurityUtils.getCurrentUser();
		if (currentUser != null) {
			log.info(
					"Getting orders, byUserId {}, current user id {}, current user role {}...",
					byUserId,
					currentUser.getId(),
					currentUser.getRole());
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

			Map<String, Object> params = new HashMap<>();
			params.put("io-service-url", staticConstants.ioServiceUrl);
			params.put("orders-endpoint", staticConstants.ioOrdersEndpoint);

			String url =
					UrlBuilder.replacePlaceholdersInString("${io-service-url}${orders-endpoint}", params);

			if (currentUser.getRole().equals(Role.USER) || ownOrders) {
				byUserId = currentUser.getId();
			}

			String urlEncoded =
					UriComponentsBuilder.fromHttpUrl(url)
							.queryParam("byUserId", byUserId)
							.encode()
							.toUriString();

			ResponseEntity<GetOrderDto[]> response;

			try {
				HttpEntity<?> entity = new HttpEntity<>(headers);
				response = restTemplate.exchange(urlEncoded, HttpMethod.GET, entity, GetOrderDto[].class);
			} catch (HttpStatusCodeException e) {
				log.error(
						"Unable to get orders, byUserId {}, current user id {}, current user role {}...",
						byUserId,
						currentUser.getId(),
						currentUser.getRole());
				return new ResponseEntity<>(
						e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
			}

			log.info(
					"Successfully got orders, byUserId {}, current user id {}, current user role {}! Total: {}!",
					byUserId,
					currentUser.getId(),
					currentUser.getRole(),
					response.getBody() == null ? null : response.getBody().length);
			return response;
		}
		log.error("Could not get current user to get orders!");
		return ResponseEntity.internalServerError().build();
	}
}
