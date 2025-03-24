package az.project.securityConfiguration.dto;

import lombok.Data;

@Data
public class PasswordResetResponse {
	private String email;
	private String token;
	private String newPassword;
}
