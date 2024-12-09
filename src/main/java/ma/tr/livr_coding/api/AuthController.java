package ma.tr.livr_coding.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.tr.livr_coding.domain.entity.User;
import ma.tr.livr_coding.dto.LoginRequest;
import ma.tr.livr_coding.dto.LoginResponse;
import ma.tr.livr_coding.dto.RefreshResponse;
import ma.tr.livr_coding.dto.RegisterRequest;
import ma.tr.livr_coding.service.auth.AuthService;
import ma.tr.livr_coding.service.jwt.JwtService;
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
    public RefreshResponse refresh(@RequestHeader("Authorization") String token) {
        return authService.refresh(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return  new ResponseEntity<>("logout success", HttpStatusCode.valueOf(200));
    }

}
