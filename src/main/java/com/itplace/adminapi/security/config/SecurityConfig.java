package com.itplace.adminapi.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itplace.adminapi.common.ApiResponse;
import com.itplace.adminapi.security.SecurityCode;
import com.itplace.adminapi.security.auth.filter.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper; // ObjectMapper 주입
    private final AuthenticationConfiguration authenticationConfiguration; // AuthenticationManager를 얻기 위해 주입

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOriginPatterns(Arrays.asList(
                                "http://localhost:3000", // 로컬 개발 환경
                                "http://localhost:5173", // 로컬 개발 환경
                                "http://localhost:5174", // 로컬 개발 환경
                                "http://localhost:8080", // 로컬 개발 환경
                                "https://admin.itplace.click",
                                "https://admin-api.itplace.click"
                        ));

                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        return configuration;
                    }
                })));

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                ); // 세션 필요 시 생성
//        http
//                .securityContext(context -> context.requireExplicitSave(false));

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                                .requestMatchers(
                                        "/hc", "/env",
                                        "/api/v1/auth/login",
                                        "/swagger-ui/**",
                                        "/swagger.yml",
                                        "/v3/api-docs/**",
                                        "/api-docs").permitAll()
                                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated());

        http
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout") // 로그아웃 처리 URL
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 로그아웃 성공 시 200 OK 상태 코드와 간단한 JSON 응답
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            objectMapper.writeValue(response.getWriter(), ApiResponse.ok(SecurityCode.LOGOUT_SUCCESS)); // 성공 코드 추가 시
                        })
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // JSESSIONID 쿠키 삭제
                );

        http
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter filter = new LoginFilter(objectMapper);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/login", "POST"));
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 로그인 성공 핸들러: 성공 시 200 OK와 메시지 응답
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return (request, response, authentication) -> {
            HttpSession session = request.getSession(true);
            request.changeSessionId();

            // SecurityContext 저장
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context
            );
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ApiResponse<Void> apiResponse = ApiResponse.ok(SecurityCode.LOGIN_SUCCESS);
            objectMapper.writeValue(response.getOutputStream(), apiResponse);
        };
    }

    // 로그인 실패 핸들러: 실패 시 401 Unauthorized와 메시지 응답
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ApiResponse<Void> apiResponse = ApiResponse.ok(SecurityCode.LOGIN_FAIL);
            objectMapper.writeValue(response.getOutputStream(), apiResponse);
        };
    }

}
