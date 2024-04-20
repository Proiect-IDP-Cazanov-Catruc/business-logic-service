/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.idp.upb.businesslogicservice.data.dto.request.OrderPostDto;
import ro.idp.upb.businesslogicservice.data.dto.response.CategoryGetDto;
import ro.idp.upb.businesslogicservice.data.dto.response.GetOrderDto;
import ro.idp.upb.businesslogicservice.data.dto.response.ProductGetDto;
import ro.idp.upb.businesslogicservice.service.StoreService;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController {
	private final StoreService storeService;

	@GetMapping("/categories")
	public List<CategoryGetDto> getCategories() {
		return storeService.getAllCategories();
	}

	@GetMapping("/products")
	public List<ProductGetDto> getProducts(@RequestParam(required = false) UUID categoryId) {
		return storeService.getProducts(categoryId);
	}

	@PostMapping("/orders")
	public GetOrderDto placeOrder(@RequestBody @Valid OrderPostDto dto) {
		return storeService.placeOrder(dto);
	}

	@GetMapping("/orders")
	public List<GetOrderDto> getOrders(
			@RequestParam(required = false) UUID byUserId,
			@RequestParam(required = false, defaultValue = "false") Boolean ownOrders) {
		return storeService.getOrders(byUserId, ownOrders);
	}
}
