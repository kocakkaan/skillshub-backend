package com.reply.skillshub.controllers.resume;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import static org.instancio.Select.all;
import static org.instancio.Select.field;

import com.reply.skillshub.controllers.resume.powerpoint.PowerPointInformation;
import com.reply.skillshub.controllers.resume.powerpoint.PowerPointService;
import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.user.User;

public class PowerPointServiceTest {

    @Test
    void testPowerPoint() {
        User user = Instancio.create(User.class);
        user.setProfilePictureLocation("pictures/Heepen_Jonas.png");
        Resume resume = Instancio.of(Resume.class)
            .generate(field(Resume::getBackground), gen -> gen.string().length(300))
            .generate(field(ResumeSkill::getSkills), gen -> gen.collection().size(10))
            .generate(all(String.class).within(field(ResumeExperience::getDescriptions).toScope()), gen -> gen.string().length(200))
            .generate(field(Resume::getExperiences),  gen -> gen.collection().maxSize(4).minSize(2))
            .generate(field(ResumeExperience::getDescriptions), gen -> gen.collection().minSize(2).maxSize(4))
            .create();
        resume.setUser(user);
        var service = new PowerPointService();
        var information = service.createPowerPointDto(resume, "en", "ML_REPLY");
        service.createPowerPoint(information);
    }

    @Test
    void testPowerPointNoResume() {
        var service = new PowerPointService();
        var information = service.createPowerPointDto(null, "en", "ML_REPLY");
        service.createPowerPoint(information);
    }

    @Test
    void testPowerPointEmptyInformationObject() {
        var service = new PowerPointService();
        var information = new PowerPointInformation();
        information.setProfilePictureLocation("pictures/Heepen_Jonas.png");
        service.createPowerPoint(information);
    }

}
