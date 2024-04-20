/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.idp.upb.businesslogicservice.data.dto.request.AddProductPost;
import ro.idp.upb.businesslogicservice.data.dto.response.ProductGetDto;
import ro.idp.upb.businesslogicservice.service.ManagerService;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

	private final ManagerService managerService;

	@PostMapping("/product")
	public ProductGetDto addProduct(@RequestBody @Valid AddProductPost dto) {
		return managerService.addProduct(dto);
	}
}
