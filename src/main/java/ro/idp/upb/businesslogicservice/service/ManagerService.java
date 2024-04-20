/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.idp.upb.businesslogicservice.data.dto.request.AddProductPost;
import ro.idp.upb.businesslogicservice.data.dto.response.ProductGetDto;
import ro.idp.upb.businesslogicservice.exception.handle.RestTemplateResponseErrorHandler;
import ro.idp.upb.businesslogicservice.utils.StaticConstants;
import ro.idp.upb.businesslogicservice.utils.UrlBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

	private final StaticConstants staticConstants;
	private final ObjectMapper objectMapper;

	@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
	public ProductGetDto addProduct(AddProductPost dto) {
		log.info(
				"Adding product [Name: {}], [Description: {}], [Price: {}], [Quantity: {}], [CategoryId: {}]...",
				dto.getName(),
				dto.getDescription().substring(0, 15),
				dto.getPrice(),
				dto.getQuantity(),
				dto.getCategoryId());

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(
				new RestTemplateResponseErrorHandler(
						objectMapper,
						() ->
								log.error(
										"Unable to add product [Name: {}], [Description: {}], [Price: {}], [Quantity: {}], [CategoryId: {}]!",
										dto.getName(),
										dto.getDescription().substring(0, 15),
										dto.getPrice(),
										dto.getQuantity(),
										dto.getCategoryId())));
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(dto, headers);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("products-endpoint", staticConstants.ioProductsEndpoint);

		String url =
				UrlBuilder.replacePlaceholdersInString("${io-service-url}${products-endpoint}", params);

		ResponseEntity<ProductGetDto> response;

		response = restTemplate.postForEntity(url, entity, ProductGetDto.class);

		log.info(
				"Successfully added product [Name: {}], [Description: {}], [Price: {}], [Quantity: {}], [CategoryId: {}]!",
				dto.getName(),
				dto.getDescription().substring(0, 15),
				dto.getPrice(),
				dto.getQuantity(),
				dto.getCategoryId());
		return response.getBody();
	}
}
