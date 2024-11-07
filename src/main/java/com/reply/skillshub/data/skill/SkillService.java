package com.reply.skillshub.data.skill;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill findById(String id) {
        return skillRepository.findById(id).orElse(null);
    }

    public Skill findByLabel(String label) {
        return skillRepository.findByLabel(label).orElse(null);
    }

    public List<Skill> findAll() {
        return skillRepository.findAll();
    }

    public List<Skill> findAllByLabel(String label) {
        return skillRepository.findAllByLabel(label);
    }
    
}
