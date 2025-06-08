package com.reply.skillshub.data.skill;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SkillService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;

    public Skill save(Skill skill) {
        logger.info("Saving skill: {}", skill.getLabel());
        return skillRepository.save(skill);
    }

    public Skill findById(String id) {
        return skillRepository.findById(id).orElse(null);
    }

    public Skill findByLabel(String label) {
        return skillRepository.findByLabel(label).orElse(null);
    }

    public Optional<Skill> findByLabelIgnoreCase(String label) {
        logger.info("Finding skill by label: {}", label);
        return skillRepository.findByLabelIgnoreCase(label);
    }

    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public List<Skill> findAllByLabel(String label) {
        return skillRepository.findAllByLabel(label);
    }
    
}
