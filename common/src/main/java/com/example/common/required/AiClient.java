package com.example.common.required;

public interface AiClient<Q, R> {
    R getChatResponse(Q request);
}


