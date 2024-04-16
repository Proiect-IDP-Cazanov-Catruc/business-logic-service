/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
