/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ro.idp.upb.businesslogicservice.data.dto.request.AddProductPost;
import ro.idp.upb.businesslogicservice.data.dto.response.ProductGetDto;
import ro.idp.upb.businesslogicservice.utils.StaticConstants;
import ro.idp.upb.businesslogicservice.utils.UrlBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

	private final StaticConstants staticConstants;

	public ResponseEntity<?> addProduct(AddProductPost dto) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<?> entity = new HttpEntity<>(dto, headers);

		Map<String, Object> params = new HashMap<>();
		params.put("io-service-url", staticConstants.ioServiceUrl);
		params.put("products-endpoint", staticConstants.ioProductsEndpoint);

		String url =
				UrlBuilder.replacePlaceholdersInString("${io-service-url}${products-endpoint}", params);

		ResponseEntity<ProductGetDto> response;

		try {
			response = restTemplate.postForEntity(url, entity, ProductGetDto.class);
		} catch (HttpStatusCodeException e) {
			log.error("Unable to add product {}!", dto.getName());
			return new ResponseEntity<>(
					e.getResponseBodyAsString(), e.getResponseHeaders(), e.getStatusCode());
		}

		log.info("Successfully added product {}!", dto.getName());
		return response;
	}
}
