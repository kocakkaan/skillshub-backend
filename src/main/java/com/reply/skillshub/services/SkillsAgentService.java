package com.reply.skillshub.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SkillsAgentService {

    private final WebClient webClient;

    private static final Logger logger = LoggerFactory.getLogger(SkillsAgentService.class);

    public SkillsAgentService(WebClient.Builder webcBuilder, @Value("${skillhub.agent}") String agent) {
        this.webClient = webcBuilder.baseUrl(agent).build();
    }

    public List<String> getKeywordsFromSearchString(String queryString) {
        try {
            logger.info("Sending query string to agent for keyword extraction: {}", queryString);
            List<String> returnValue = webClient
            .get()
            .uri((uriBuilder) -> uriBuilder.path("/keywords").queryParam("text", queryString).build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
            .block();
            logger.info("Received keywords from agent: {}", returnValue);
            return returnValue;
        } catch (Exception e) {
            logger.error("Failed to extract keywords from query string", e);
            return new ArrayList<>();
        }
    }

    public CvInformation extractInformationFromCvPdf(MultipartFile cvPdf) {
        try {
            logger.info("Sending CV PDF to agent for extraction");
            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("file", cvPdf.getResource());


            var returnValue = webClient
            .post()
            .uri("/resume-extraction")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(parts)
            .retrieve()
            .bodyToMono(CvInformation.class)
            .block();
            logger.info("Received CV information based on PDF from agent");
            return returnValue;
        } catch (Exception e) {
            logger.error("Failed to extract information from CV PDF", e);
            return new CvInformation();
        }
    }

    public Optional<GeneratedShortCv> generateShortCv(String userId, String requirements) {
        logger.info("Sent short CV to agent for userId: {}, requirements: {}", userId, requirements);
        try {
            var returnValue = webClient
            .get()
            .uri((uriBuilder) -> uriBuilder.path("/short-cv-generation").queryParam("user_id", userId).queryParam("requirements", requirements).build())
            .retrieve()
            .bodyToMono(GeneratedShortCv.class)
            .block();
            logger.info("Received short CV from agent for userId: {}", userId);
            return Optional.of(returnValue);
        } catch (Exception e) {
            logger.error("Failed to generate short CV", e);
            return Optional.empty();
        }
    }
}