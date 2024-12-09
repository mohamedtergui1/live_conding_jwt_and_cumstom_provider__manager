package ma.tr.livr_coding.security;

import lombok.RequiredArgsConstructor;
import ma.tr.livr_coding.repository.UserRepository;
import ma.tr.livr_coding.service.jwt.JwtServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfigBeans {

    private final UserRepository userRepository;
    private final JwtServiceImpl jwtService;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public JwtAuthProvider jwtAuthProvider() {
        return new JwtAuthProvider(jwtService, userDetailsService());
    }

    @Bean
    public List<AuthenticationProvider> authenticationProviderList() {
        return List.of(daoAuthenticationProvider(), jwtAuthProvider());
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManagerImpl(authenticationProviderList());
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(authenticationManager());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(s -> s.disable()).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated())
                .authenticationManager(authenticationManager())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class).build();
    }

}
