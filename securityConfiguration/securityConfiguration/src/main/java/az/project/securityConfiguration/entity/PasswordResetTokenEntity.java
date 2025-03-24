package az.project.securityConfiguration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PasswordResetTokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String token;

	private String email;

	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne
	private UserEntity user;
	@Column(nullable = false)
	private LocalDateTime expirationDate;
}