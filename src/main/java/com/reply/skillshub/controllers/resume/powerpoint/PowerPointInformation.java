package com.reply.skillshub.controllers.resume.powerpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class PowerPointInformation {

    private String position = "";
    private String companyLogo = "";
    private String profilePictureLocation = "pictures/Heepen_Jonas.png";
    private String company = "";
    private String language = "";
    private String name = "";
    private String email = "";
    private String phone = "";
    private String role = "";
    private String title = "";
    private String background = "";
    private List<String> industries = new ArrayList<>();
    private List<PowerPointExperience> experiences = new ArrayList<>();
    private List<PowerPointSkill> skills = new ArrayList<>();


    @Data
    public static class PowerPointExperience {
        private String title;
        private String position;
        private List<String> descriptions = new ArrayList<>();
    }

    @Data
    public static class PowerPointSkill {
        private String parentSkill;
        private List<String> childSkills;
    }
    
}
