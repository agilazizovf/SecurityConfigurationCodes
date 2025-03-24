package az.project.securityConfiguration.controller;

import az.project.securityConfiguration.dto.PasswordResetRequest;
import az.project.securityConfiguration.dto.PasswordResetResponse;
import az.project.securityConfiguration.dto.ResetRequest;
import az.project.securityConfiguration.dto.ResetResponse;
import az.project.securityConfiguration.service.PasswordResetTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/passwords")
@RequiredArgsConstructor
public class PasswordResetTokenController {
	private final PasswordResetTokenService passwordResetTokenService;

	@PostMapping("/reset-request")
	public ResponseEntity<ResetResponse> resetRequest(@RequestBody ResetRequest request) throws MessagingException {
		passwordResetTokenService.initiatePasswordReset(request.getEmail());
		ResetResponse response = new ResetResponse();
		response.setMessage("Password reset link has been sent to your email.");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/reset")
	public PasswordResetResponse resetPassword(@RequestBody PasswordResetRequest request) {
		PasswordResetResponse response = passwordResetTokenService.resetPassword(request.getEmail(), request.getToken(),
				request.getNewPassword());
		return response;
	}

}
