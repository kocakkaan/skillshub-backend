package com.reply.skillshub.data.experience;

import com.reply.skillshub.data.user.User;

public class ExperienceTestData {

    public static Experience returnValidExperience() {
        Experience experience = new Experience();
        experience.setTitle("Test");
        experience.getEmployees().add(returnUserWithEmail());
        return experience;
    }

    private static User returnUserWithEmail() {
        User personToSave1 = new User();
        personToSave1.setFirstName("FirstName");
        personToSave1.setLastName("LastName");
        personToSave1.setEmail("maurits.de.roover@reply.com");
        return personToSave1;
    }
    
}
