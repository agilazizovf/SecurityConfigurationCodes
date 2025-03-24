package az.project.securityConfiguration.service;

import az.project.securityConfiguration.dto.PasswordResetResponse;
import az.project.securityConfiguration.entity.PasswordResetTokenEntity;
import az.project.securityConfiguration.entity.UserEntity;
import az.project.securityConfiguration.exception.CustomException;
import az.project.securityConfiguration.repository.PasswordResetTokenRepository;
import az.project.securityConfiguration.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {
	private final UserRepository userRepository;
	private final PasswordResetTokenRepository tokenRepository;
	private final JavaMailSender mailSender;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void initiatePasswordReset(String email) throws MessagingException {
		UserEntity currentUser = userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException("Istifadəçi tapılmadı", "User not found!", "Not found", 404, null));
		Optional<UserEntity> user = userRepository.findByEmail(currentUser.getEmail());
		if (user.isEmpty()) {
			throw new CustomException("İstifadəçi müştəri ilə əlaqəli deyil", "User not associated with client",
					"Not associated", 422, null);
		}
		// Delete any existing token for the user
		tokenRepository.deleteByUser_Email(user.get().getEmail());
		// Generate and save a new token with expiration date
		String token = generateAndSaveActivationToken(currentUser.getEmail());
		// Send the reset email
		sendResetEmail(currentUser.getEmail(), token);
	}

	public void sendResetEmail(String email, String token) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(email);
		helper.setSubject("Şifrəni Sıfırlama");

		String content = "\n\n\u015eifrənizi sıfırlamaq üçün bu kodu daxil edin: " + token + "\n\n" +
				"Bu kod sadəcə bir dəfə istifadə edilə bilər.";

		helper.setText(content);
		helper.setFrom("your-email@gmail.com");

		mailSender.send(message);
		System.out.println("Reset Email Sent!");
	}

	@Transactional
	public PasswordResetResponse resetPassword(String email, String token, String newPassword) {
		PasswordResetTokenEntity resetToken = tokenRepository.findByEmailAndToken(email, token).orElseThrow(
				() -> new CustomException("Etibarsız token və ya email", "Invalid token or email", "Invalid", 400, null));

		if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
			throw new CustomException("Tokenin vaxtı bitdi", "Token has expired", "Expired", 400, null);
		}

		UserEntity user = userRepository.findByEmail(resetToken.getUser().getEmail())
				.orElseThrow(() -> new CustomException("Istifadəçi tapılmadı", "User not found!", "Not found", 404, null));

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

		// Optionally, delete the token after use
		tokenRepository.deleteByToken(token);

		// Prepare the response
		PasswordResetResponse response = new PasswordResetResponse();
		response.setEmail(email);
		response.setToken(token);
		response.setNewPassword(newPassword);

		return response;
	}

	private String generateAndSaveActivationToken(String email) {
		String generatedToken = generateActivationCode();

		// Find the user associated with the email
		UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException("Istifadəçi tapılmadı", "User not found!", "Not found", 404, null));

		// Create and save the token
		PasswordResetTokenEntity token = PasswordResetTokenEntity.builder().token(generatedToken)
				.expirationDate(LocalDateTime.now().plusMinutes(15)) // Set expiration date here
				.user(user) // Associate the user
				.email(email) // Set the email
				.build();

		tokenRepository.save(token);

		return generatedToken;
	}

	private String generateActivationCode() {
		String characters = "0123456789";
		StringBuilder codeBuilder = new StringBuilder();
		SecureRandom secureRandom = new SecureRandom();
		for (int i = 0; i < 6; i++) {
			int randomIndex = secureRandom.nextInt(characters.length());
			codeBuilder.append(characters.charAt(randomIndex));
		}
		return codeBuilder.toString();
	}
}