package ma.tr.livr_coding.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.tr.livr_coding.domain.entity.User;
import ma.tr.livr_coding.dto.LoginRequest;
import ma.tr.livr_coding.dto.LoginResponse;
import ma.tr.livr_coding.dto.RefreshResponse;
import ma.tr.livr_coding.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    User register(RegisterRequest registerRequest);

    RefreshResponse refresh(String refreshToken);

    void logout();
}
