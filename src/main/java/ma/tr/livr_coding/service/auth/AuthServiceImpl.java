package ma.tr.livr_coding.service.auth;

import lombok.RequiredArgsConstructor;
import ma.tr.livr_coding.dto.*;
import ma.tr.livr_coding.entity.User;
import ma.tr.livr_coding.mapper.AuthMapper;
import ma.tr.livr_coding.repository.UserRepository;
import ma.tr.livr_coding.service.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;
    private final JwtService jwtService;


    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        if (authenticate.getPrincipal() instanceof User user) {

            return new LoginResponse(user, jwtService.generateToken(user), jwtService.generateTokenRefresh(user), jwtService.getExpirationTime());
        }
        throw new RuntimeException("can ' t cast auth to user");
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new RuntimeException("email already used");
        }

        User user = authMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public RefreshResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        if (!jwtService.isRefreshToken(refreshTokenRequest.refreshToken())) {
            throw new RuntimeException("refresh token is not valid");
        }
        String email = jwtService.extractEmail(refreshTokenRequest.refreshToken());
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
        if (jwtService.isTokenValid(refreshTokenRequest.refreshToken(), user)) {
            return new RefreshResponse(jwtService.getExpirationTime(), jwtService.generateToken(user));
        }
        throw new RuntimeException(" go to login");
    }


}
