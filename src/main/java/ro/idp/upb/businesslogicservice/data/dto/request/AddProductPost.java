/* Ionel Catruc 343C3, Veaceslav Cazanov 343C3 | IDP BUSINESS-LOGIC-SERVICE | (C) 2024 */
package ro.idp.upb.businesslogicservice.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductPost {

	@NotBlank(message = "Product name should not be blank")
	private String name;

	@NotBlank(message = "Product description should not be blank")
	private String description;

	@NotNull(message = "Product price should be provided") private Double price;

	@NotNull(message = "Product quantity should be provided") private Integer quantity;

	@NotNull(message = "Product category id should be provided") private UUID categoryId;
}
