/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.idp.upb.businesslogicservice.data.dto.request.OrderPostDto;
import ro.idp.upb.businesslogicservice.service.StoreService;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController {
	private final StoreService storeService;

	@GetMapping("/categories")
	public ResponseEntity<?> getCategories() {
		return storeService.getAllCategories();
	}

	@GetMapping("/products")
	public ResponseEntity<?> getProducts(@RequestParam(required = false) UUID categoryId) {
		return storeService.getProducts(categoryId);
	}

	@PostMapping("/orders")
	public ResponseEntity<?> placeOrder(@RequestBody @Valid OrderPostDto dto)
			throws AuthenticationException {
		return storeService.placeOrder(dto);
	}

	@GetMapping("/orders")
	public ResponseEntity<?> getOrders(
			@RequestParam(required = false) UUID byUserId,
			@RequestParam(required = false, defaultValue = "false") Boolean ownOrders)
			throws AuthenticationException {
		return storeService.getOrders(byUserId, ownOrders);
	}
}
