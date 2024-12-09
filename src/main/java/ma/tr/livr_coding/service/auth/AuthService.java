package ma.tr.livr_coding.service.auth;

import ma.tr.livr_coding.dto.*;
import ma.tr.livr_coding.entity.User;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    User register(RegisterRequest registerRequest);

    RefreshResponse refresh(RefreshTokenRequest refreshTokenRequest);

}
