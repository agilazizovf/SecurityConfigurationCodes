package az.project.securityConfiguration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginRequest {

    @NotBlank(message = "Username cannot be empty or null")
    @Size(max = 50)
    @Pattern(regexp = "[A-Za-z0-9_.]+$")
    private String username;

    @NotBlank(message = "Password cannot be empty or null")
    @Size(min = 3)
    @Pattern(regexp = "[A-Za-z0-9_.]+")
    private String password;
}
