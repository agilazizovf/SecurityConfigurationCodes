package az.project.securityConfiguration.service;


import az.project.securityConfiguration.dto.ClientRequest;
import az.project.securityConfiguration.dto.ExceptionDTO;
import az.project.securityConfiguration.dto.LoginRequest;
import az.project.securityConfiguration.dto.LoginResponse;
import az.project.securityConfiguration.entity.Authority;
import az.project.securityConfiguration.entity.Client;
import az.project.securityConfiguration.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public ResponseEntity<?> authenticate(LoginRequest loginReq){
        log.info("authenticate method started by: {}", loginReq.getUsername());
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(),
                            loginReq.getPassword()));
            log.info("authentication details: {}", authentication);
            String username = authentication.getName();
            Client client = new Client(username,"");
            String token = jwtUtil.createToken(client);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            LoginResponse loginRes = new LoginResponse(username,token);
            log.info("user: {} logged in",  client.getUsername());
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(loginRes);

        }catch (BadCredentialsException e){
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(),"Invalid username or password");
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        }catch (Exception e){
            ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            log.error("Error due to {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionDTO);
        }
    }

    public void register(ClientRequest clientReq){
        Client client = new Client();
        client.setUsername(clientReq.getUsername());
        client.setPassword(passwordEncoder.encode(clientReq.getPassword()));
        Authority authority = new Authority("USER");
        Set<Authority> authoritySet = Set.of(authority);
        client.setAuthorities(authoritySet);
        clientRepository.save(client);
        log.info("saved new client");
    }

}
