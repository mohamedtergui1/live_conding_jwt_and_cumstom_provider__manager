package ma.tr.livr_coding.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.tr.livr_coding.dto.*;
import ma.tr.livr_coding.entity.User;
import ma.tr.livr_coding.service.auth.AuthService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public User login(@RequestBody @Valid RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/auth/login")
    public LoginResponse signUp(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/auth/refresh")
    public RefreshResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refresh(refreshTokenRequest);
    }

}
