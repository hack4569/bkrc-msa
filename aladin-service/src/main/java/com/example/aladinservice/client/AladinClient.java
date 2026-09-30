package com.example.aladinservice.client;

import com.example.aladinservice.application.request.AladinRequest;
import com.example.aladinservice.application.response.AladinResponse;
import com.example.aladinservice.entity.AladinBook;
import com.example.aladinservice.entity.AladinConstants;
import com.example.aladinservice.exception.AladinClientException;
import com.example.common.ErrorCode;
import com.example.common.exception.BusinessException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;

@Component
public class AladinClient {
    private final String host;
    private final String ttbKey;
    private final Duration connectTimeout;
    private final Duration readTimeout;
    private RestClient restClient;

    public AladinClient(
            @Value("${aladin.host}") String host,
            @Value("${aladin.ttbkey}") String ttbKey,
            @Value("${external-api.aladin.connect-timeout:3s}") Duration connectTimeout,
            @Value("${external-api.aladin.read-timeout:5s}") Duration readTimeout
    ) {
        this.host = host;
        this.ttbKey = ttbKey;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    @PostConstruct
    void initialize() {
        JsonMapper mapper = JsonMapper.builder().enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS).build();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);
        restClient = RestClient.builder()
                .baseUrl(host)
                .requestFactory(requestFactory)
                .messageConverters(converters -> {
                    converters.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
                    converters.add(new MappingJackson2HttpMessageConverter(mapper));
                })
                .build();
    }

    public AladinResponse request(String path, AladinRequest request) {
        try {
            return restClient.get()
                    .uri(builder -> builder.path(path).queryParam("ttbkey", ttbKey)
                            .queryParams(request.toQueryParams()).build())
                    .retrieve()
                    .body(AladinResponse.class);
        } catch (Exception exception) {
            throw new AladinClientException(exception);
        }
    }

    public List<AladinBook> search(String query) {
        AladinResponse response = request(AladinConstants.ITEM_SEARCH, AladinRequest.search(query));
        return response == null || response.item() == null ? List.of() : response.item();
    }

    public AladinBook detail(String isbn13) {
        AladinResponse response = request(AladinConstants.ITEM_LOOKUP, AladinRequest.detail(isbn13));
        if (response == null || response.item() == null || response.item().isEmpty()) {
            throw new BusinessException(ErrorCode.ALADIN_NOT_FOUND);
        }
        return response.item().getFirst();
    }
}
