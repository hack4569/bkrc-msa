package com.example.apigatewayservice.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MemberHeaderGlobalFilter implements GlobalFilter, Ordered {

    public static final String MEMBER_ID_HEADER = "X-Member-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .filter(Authentication.class::isInstance)
                .map(Authentication.class::cast)
                .filter(Authentication::isAuthenticated)
                .map(authentication -> String.valueOf(authentication.getPrincipal()))
                .defaultIfEmpty("")
                .flatMap(memberId -> chain.filter(withTrustedMemberHeader(exchange, memberId)));
    }

    private ServerWebExchange withTrustedMemberHeader(ServerWebExchange exchange, String memberId) {
        return exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .headers(headers -> {
                            headers.remove(MEMBER_ID_HEADER);
                            if (!memberId.isBlank()) {
                                headers.set(MEMBER_ID_HEADER, memberId);
                            }
                        })
                        .build())
                .build();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
