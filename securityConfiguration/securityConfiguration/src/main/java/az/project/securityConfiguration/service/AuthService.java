package az.project.securityConfiguration.service;

import az.project.securityConfiguration.dto.AuthRequest;
import az.project.securityConfiguration.dto.AuthResponse;
import az.project.securityConfiguration.exception.CustomException;
import az.project.securityConfiguration.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws Exception {
        String username = authRequest.getUsername();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new CustomException("İstifadəçi adı və ya şifrə yanlışdır", "Invalid username or password",
                    "Authenticated", 403, null);
        }

        // Fetch user details from the database
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Generate JWT token
        final String jwt = jwtUtil.generateToken(userDetails);

        // Extract the roles from the userDetails
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Create the AuthResponse containing the username, token, and roles
        AuthResponse response = new AuthResponse(username, jwt, roles);

        return ResponseEntity.ok(response);
    }

}
