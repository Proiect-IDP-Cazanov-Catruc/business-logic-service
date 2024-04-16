/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostManagerDto {
	@NotBlank private String email;
	@NotBlank private String firstName;
	@NotBlank private String lastName;
}
