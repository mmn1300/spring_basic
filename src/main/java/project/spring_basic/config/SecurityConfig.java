package project.spring_basic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import project.spring_basic.api.custom.CustomEntryPoint.CustomAuthErrorEntryPoint;
import project.spring_basic.api.custom.CustomHandler.CustomAccessDeniedHandler;
import project.spring_basic.api.custom.CustomHandler.CustomAuthSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthSuccessHandler customAuthSuccessHandler;

    @Autowired
    private CustomAuthErrorEntryPoint customAuthErrorEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;



    // AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    // BCrypt 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 쿠키 CSRF 토큰 및 인증 관련
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.GET).permitAll()
                
                // 로그인 관련
                .requestMatchers(HttpMethod.POST,"/account/check").permitAll()
                .requestMatchers(HttpMethod.POST,"/account/login").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/session").permitAll()

                // 계정 관련
                .requestMatchers(HttpMethod.POST,"/account/member").permitAll()
                .requestMatchers(HttpMethod.PUT, "/account/**").hasRole("USER")

                // 리소스 관련
                .requestMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()

                // 그 외 인증 필요
                .anyRequest().authenticated()
            );

        http
            .formLogin((auth) -> auth
                .loginPage("/login")
                .loginProcessingUrl("/account/login")
                .successHandler(customAuthSuccessHandler)
            );

        http
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthErrorEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            );

        return http.build();
    }


    // CORS 관련
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowCredentials(true)
                    .allowedHeaders("*");
            }
        };
    }
}
