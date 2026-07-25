package com.reply.skillshub.controllers.occupations;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.OccupationsApi;
import com.reply.skillshub.openapi.model.OccupationalCategoryDto;
import com.reply.skillshub.openapi.model.OccupationsPostRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OccupationsController implements OccupationsApi {

    private final OccupationsControllerService occupationControllerService;

    @Override
    public ResponseEntity<List<OccupationalCategoryDto>> occupationsGet(Optional<String> label) {
        return ResponseEntity.ok(occupationControllerService.getOccupations(label));
    }

    @Override
    public ResponseEntity<OccupationalCategoryDto> occupationsPost(OccupationsPostRequest occupationsPostRequest) {
        return ResponseEntity.status(201).body(occupationControllerService.addOccupation(occupationsPostRequest));
    }

}
