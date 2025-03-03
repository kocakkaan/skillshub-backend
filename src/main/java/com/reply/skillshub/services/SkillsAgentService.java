package com.reply.skillshub.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Service
public class SkillsAgentService {

    private final WebClient webClient;

    public SkillsAgentService(WebClient.Builder webcBuilder, @Value("${skillhub.agent}") String agent) {
        this.webClient = webcBuilder.baseUrl(agent).build();
    }

    public List<String> getKeywordsFromSearchString(String queryString) {
        try {
            List<String> returnValue = webClient
            .get()
            .uri((uriBuilder) -> uriBuilder.path("/keywords").queryParam("text", queryString).build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
            .block();
            return returnValue;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}