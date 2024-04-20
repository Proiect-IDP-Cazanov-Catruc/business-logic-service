/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.idp.upb.businesslogicservice.data.dto.request.PostManagerDto;
import ro.idp.upb.businesslogicservice.data.dto.response.UserDto;
import ro.idp.upb.businesslogicservice.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

	private final UserService userService;

	@PostMapping("/manager")
	public UserDto createManager(@RequestBody @Valid PostManagerDto dto) {
		return userService.createManager(dto);
	}
}
