package com.reply.skillshub.controllers.skill;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.SkillsApi;
import com.reply.skillshub.openapi.model.SkillDto;
import com.reply.skillshub.openapi.model.SkillsPostRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SkillController implements SkillsApi {
    
    private final SkillControllerService skillControllerService;

    @Override
    public ResponseEntity<List<SkillDto>> skillsGet(Optional<String> label) {
        return ResponseEntity.ok(skillControllerService.getSkills(label));
    }

    @Override
    public ResponseEntity<SkillDto> skillsPost(SkillsPostRequest skillsPostRequest) {
        return ResponseEntity.status(201).body(skillControllerService.addSkill(skillsPostRequest));
    }
    
}
