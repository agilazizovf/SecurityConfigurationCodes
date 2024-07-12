package az.project.securityConfiguration.controller;


import az.project.securityConfiguration.dto.ClientRequest;
import az.project.securityConfiguration.dto.LoginRequest;
import az.project.securityConfiguration.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginReq)  {
        return authService.authenticate(loginReq);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid ClientRequest clientReq)  {
        authService.register(clientReq);
    }
}
