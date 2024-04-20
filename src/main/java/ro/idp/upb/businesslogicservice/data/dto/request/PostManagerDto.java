/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostManagerDto {
	@NotBlank(message = "Email should be provided")
	@Email(message = "Provided email is not actually an email")
	private String email;

	@NotBlank(message = "First name should be provided")
	private String firstName;

	@NotBlank(message = "Last name should be provided")
	private String lastName;
}
