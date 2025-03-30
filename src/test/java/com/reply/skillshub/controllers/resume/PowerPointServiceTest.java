package com.reply.skillshub.controllers.resume;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import com.reply.skillshub.controllers.resume.powerpoint.PowerPointInformation;
import com.reply.skillshub.controllers.resume.powerpoint.PowerPointService;
import com.reply.skillshub.data.resume.ShortCv;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;

class PowerPointServiceTest {

    @Test 
    void testCreatePowerPointFromTemplate() {
        var user = Instancio.create(BaseUserTest.class);
        user.setProfilePictureLocation("pictures/test.jpg");
        ShortCv resume = Instancio.of(ShortCv.class)
            .generate(field(ShortCv::getBackground), gen -> gen.string().length(300))
            .generate(field(ResumeSkill::getSkills), gen -> gen.collection().size(10))
            .generate(all(String.class).within(field(ResumeExperience::getDescriptions).toScope()), gen -> gen.string().length(200).lowerCase())
            .generate(field(ShortCv::getExperiences),  gen -> gen.collection().maxSize(4).minSize(2))
            .generate(field(ResumeExperience::getDescriptions), gen -> gen.collection().minSize(2).maxSize(4))
            .create();
        var service = new PowerPointService();
        var information = service.createPowerPointDto(user, resume, "de", "Reply");
        var ppt = service.createPowerPointFromTemplate(information);
        savePowerPoint(ppt);
    }

    private void savePowerPoint(XMLSlideShow ppt) {
        try {
            var test = new FileOutputStream("test.pptx");
            ppt.write(test);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    void testPowerPointNoResume() {
        var service = new PowerPointService();
        var information = service.createPowerPointDto(null, null, "en", "ML_REPLY");
        service.createPowerPointFromTemplate(information);
    }

    @ParameterizedTest
    @MethodSource("provideIncompleteResumes")
    void testPowerPointIncompleteResume(ShortCv resume) {
        var service = new PowerPointService();
        var user = Instancio.create(BaseUserTest.class);
        var information = service.createPowerPointDto(user, resume, "en", "ML_REPLY");
        service.createPowerPointFromTemplate(information);
    }

    private static Stream<Arguments> provideIncompleteResumes() {
        var startingPoint = Instancio.of(ShortCv.class);
        var completeResume = startingPoint.create();
        var noBackground = startingPoint.set(field(ShortCv::getBackground), null).create();
        var noSkills = startingPoint.set(field(ShortCv::getSkills), List.of()).create();
        var noExperiences = startingPoint.set(field(ShortCv::getExperiences), List.of()).create();
        var noIndustries = startingPoint.set(field(ShortCv::getIndustries), List.of()).create();
        return Stream.of(
            Arguments.of(completeResume),
            Arguments.of(noBackground),
            Arguments.of(noSkills),
            Arguments.of(noExperiences),
            Arguments.of(noIndustries)
        );
    }

    

    @Test
    void testPowerPointEmptyInformationObject() {
        var service = new PowerPointService();
        var information = new PowerPointInformation();
        information.setProfilePictureLocation("pictures/Heepen_Jonas.png");
        service.createPowerPointFromTemplate(information);
    }

    @Test
    void testPowerPointWithNullAndEmptyLists() {
        var service = new PowerPointService();
        var information = Instancio.of(PowerPointInformation.class)
            .set(all(String.class), null)
            .supply(all(List.class), () -> List.of())
            .create();
        service.createPowerPointFromTemplate(information);
    }

    @Test
    void testPowerPointRandomProfilePictureLocation() {
        var service = new PowerPointService();
        var information = Instancio.of(PowerPointInformation.class)
            .generate(field(PowerPointInformation::getSkills), gen -> gen.collection().size(10))
            .generate(field(PowerPointInformation::getExperiences), gen -> gen.collection().size(4))
            .create();
        service.createPowerPointFromTemplate(information);
    }

}
