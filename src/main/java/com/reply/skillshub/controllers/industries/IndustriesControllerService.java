package com.reply.skillshub.controllers.industries;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.industry.Industry;
import com.reply.skillshub.data.industry.IndustryService;
import com.reply.skillshub.openapi.model.IndustryDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndustriesControllerService {
    
    private final IndustryService industryService;

    public List<IndustryDto> getIndustries(Optional<String> label) {
        List<Industry> industries;
        if (label.isPresent()) {
            industries = industryService.findByLabel(label.get());
        } else {
            industries = industryService.findAllIndustries();
        }
        return industries.stream().map(this::convertToIndustryDto).toList();
    }

    private IndustryDto convertToIndustryDto(Industry industry) {
        return new IndustryDto()
            .id(industry.getId())
            .label(industry.getLabel());
    }
}
