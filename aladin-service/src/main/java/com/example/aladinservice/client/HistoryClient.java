package com.example.aladinservice.client;

import com.example.aladinservice.application.response.HistoryResponse;
import com.example.common.security.GatewayMemberAuthenticationFilter;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class HistoryClient {
    private final RestClient restClient;

    public HistoryClient(@LoadBalanced RestClient.Builder loadBalancedRestClientBuilder) {
        this.restClient = loadBalancedRestClientBuilder.baseUrl("http://HISTORY-SERVICE").build();
    }

    public List<HistoryResponse> findByMemberId(Long memberId) {
        List<HistoryResponse> result = restClient.get()
                .uri("/history-service/internal/v1/histories/{memberId}", memberId)
                .header(GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER, memberId.toString())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }

    public void deleteByMemberId(Long memberId) {
        restClient.delete()
                .uri("/history-service/internal/v1/histories/{memberId}", memberId)
                .header(GatewayMemberAuthenticationFilter.MEMBER_ID_HEADER, memberId.toString())
                .retrieve()
                .toBodilessEntity();
    }
}
