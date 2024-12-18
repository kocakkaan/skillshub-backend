package com.reply.skillshub.controllers.industries;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.IndustriesApi;
import com.reply.skillshub.openapi.model.IndustryDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IndustriesController implements IndustriesApi {

    private final IndustriesControllerService industriesControllerService;
    
    @Override
    public ResponseEntity<List<IndustryDto>> industriesGet(Optional<String> label) {
        return ResponseEntity.ok(industriesControllerService.getIndustries(label));
    }
    
}
