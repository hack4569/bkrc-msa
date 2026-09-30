package com.example.apigatewayservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class WebSecurity {

    private final SecretKey secretKey;

    public WebSecurity(@Value("${token.secret}") String tokenSecret) {
        this.secretKey = Keys.hmacShaKeyFor(tokenSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(org.springframework.security.config.web.server.ServerHttpSecurity http) {
        return http
                .csrf(org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/actuator/health", "/error").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers("/user-service/v3/api-docs/**").permitAll()
                        .pathMatchers("/history-service/swagger-ui/**", "/history-service/v3/api-docs/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/user-service/v1/member").permitAll()
                        .pathMatchers(HttpMethod.POST, "/user-service/login").permitAll()
                        .pathMatchers("/user-service/**", "/history-service/**", "/like-service/**").authenticated()
                        .anyExchange().denyAll()
                )
                .build();
    }

    private AuthenticationWebFilter jwtAuthenticationFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtAuthenticationManager());
        filter.setServerAuthenticationConverter(new BearerAuthenticationConverter());
        filter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        filter.setAuthenticationFailureHandler((exchange, exception) -> {
            exchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getExchange().getResponse().setComplete();
        });
        return filter;
    }

    private org.springframework.security.authentication.ReactiveAuthenticationManager jwtAuthenticationManager() {
        return authentication -> {
            String token = String.valueOf(authentication.getCredentials());
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                String memberId = claims.getSubject();
                if (memberId == null || memberId.isBlank()) {
                    return Mono.error(new JwtException("JWT subject is missing") {});
                }
                return Mono.just(new UsernamePasswordAuthenticationToken(
                        Long.valueOf(memberId), token, AuthorityUtils.NO_AUTHORITIES));
            } catch (JwtException | IllegalArgumentException exception) {
                return Mono.error(exception);
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("token", "userId"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private static final class BearerAuthenticationConverter implements ServerAuthenticationConverter {
        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                return Mono.empty();
            }
            String token = authorization.substring(7).trim();
            return token.isEmpty()
                    ? Mono.empty()
                    : Mono.just(new UsernamePasswordAuthenticationToken(null, token));
        }
    }
}
