package com.reply.skillshub.controllers.skill;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.skill.Skill;
import com.reply.skillshub.data.skill.SkillService;
import com.reply.skillshub.openapi.model.SkillDto;
import com.reply.skillshub.openapi.model.SkillsPostRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillControllerService {

    private final SkillService skillService;

    public List<SkillDto> getSkills(Optional<String> label) {
        if (label.isPresent()) {
            return skillService.findAllByLabel(label.get()).stream().map(this::convertToSkillDto).toList();
        } else {
            return skillService.findAll().stream().map(this::convertToSkillDto).toList();
        }
    }

    public SkillDto addSkill(SkillsPostRequest skillsPostRequest) {
        var newSkill = new Skill();
        newSkill.setLabel(skillsPostRequest.getLabel());
        return convertToSkillDto(skillService.save(newSkill));
    }

    private SkillDto convertToSkillDto(Skill skill) {
        return new SkillDto().id(skill.getId()).label(skill.getLabel());
    }
    
}
