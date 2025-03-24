package az.project.securityConfiguration.controller;


import az.project.securityConfiguration.dto.AuthRequest;
import az.project.securityConfiguration.exception.CustomException;
import az.project.securityConfiguration.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginReq, BindingResult br) throws Exception {
        if (br.hasErrors()) {
            throw new CustomException("Məlumatların tamlığı pozulub!", "Validation failed!", "Validation", 400, br);
        }
        return authService.login(loginReq);
    }
}
