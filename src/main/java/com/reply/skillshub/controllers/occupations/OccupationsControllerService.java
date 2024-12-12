package com.reply.skillshub.controllers.occupations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.occupation.Occupation;
import com.reply.skillshub.data.occupation.OccupationService;
import com.reply.skillshub.openapi.model.OccupationalCategoryDto;
import com.reply.skillshub.openapi.model.OccupationsPostRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OccupationsControllerService {

    private final OccupationService occupationService;

    public List<OccupationalCategoryDto> getOccupations(Optional<String> label) {
        if (label.isPresent()) {
            return occupationService.findAllByLabel(label.get()).stream().map(this::convertToOccupationDto).toList();
        } else {
            return occupationService.findAll().stream().map(this::convertToOccupationDto).toList();
        }
    }

    public OccupationalCategoryDto addOccupation(OccupationsPostRequest occupationPostRequest) {
        var newOccupation = new Occupation();
        newOccupation.setLabel(occupationPostRequest.getLabel());
        return convertToOccupationDto(occupationService.save(newOccupation));
    }

    private OccupationalCategoryDto convertToOccupationDto(Occupation occupation) {
        return new OccupationalCategoryDto().id(occupation.getId()).label(occupation.getLabel());
    }
    
}
