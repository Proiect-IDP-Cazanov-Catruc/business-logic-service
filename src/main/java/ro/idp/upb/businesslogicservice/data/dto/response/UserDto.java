package ro.idp.upb.businesslogicservice.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ro.idp.upb.businesslogicservice.data.enums.Role;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
