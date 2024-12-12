package com.reply.skillshub.data.occupation;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OccupationService {

    private final OccupationRepository occupationRepository;

    public Occupation save(Occupation occupation) {
        return occupationRepository.save(occupation);
    }

    public Occupation findById(String id) {
        return occupationRepository.findById(id).orElse(null);
    }

    public Occupation findByLabel(String label) {
        return occupationRepository.findByLabel(label).orElse(null);
    }

    public List<Occupation> findAll() {
        return occupationRepository.findAll();
    }

    public List<Occupation> findAllByLabel(String label) {
        return occupationRepository.findAllByLabel(label);
    }
    
}
