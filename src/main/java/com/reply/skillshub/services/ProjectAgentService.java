package com.reply.skillshub.services;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;

@Service
public class ProjectAgentService {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(ProjectAgentService.class);

    public ProjectAgentService(WebClient.Builder webcBuilder, @Value("${skillhub.agent}") String agentBaseUrl) {
        this.webClient = webcBuilder.baseUrl(agentBaseUrl).build();
    }

    public void processProject(String projectId) {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder.path("/process-project")
                            .queryParam("project_id", projectId)
                            .build())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            stopWatch.stop();
            logger.info("Successfully sent projectId {} to /process-project in {} ms", projectId,
                    stopWatch.getTotalTimeMillis());
        } catch (Exception e) {
            logger.error("Failed to process project with ID '{}': {}", projectId, e.getMessage(), e);
        }
    }

    public List<String> searchProjectsByQuery(String searchQuery, Integer topK) {
        try {
            logger.info("Sending search query '{}', topK '{}' to agent /search-projects", searchQuery, topK);
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String encodedSearchQuery = UriUtils.encodeQueryParam(searchQuery, StandardCharsets.UTF_8);
            List<String> result = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/search-projects")
                            .queryParam("query_string", encodedSearchQuery)
                            .queryParam("top_k", topK)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<String>>() {
                    })
                    .block();
            stopWatch.stop();
            logger.info("Agent search for projects completed in {} ms", stopWatch.getTotalTimeMillis());
            return result;
        } catch (Exception e) {
            logger.error("Failed to search projects with query '{}': {}", searchQuery, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
