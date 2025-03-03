package com.reply.skillshub.controllers.resume.powerpoint;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.user.BaseUser;

@Service
public class PowerPointService {

  public PowerPointInformation createPowerPointDto(BaseUser baseUser, Resume resume, String language, String company) {
    return createPowerPointDto(baseUser, resume, language, company, false);
  }

  public PowerPointInformation createPowerPointDto(BaseUser baseUser, Resume resume, String language, String company, boolean anonymous) {
    PowerPointInformation powerPointInformation = new PowerPointInformation();

    if (resume == null) {
      return powerPointInformation;
    }

    resume.getOptionalRole().ifPresent(role -> powerPointInformation.setPosition(role.getLabel()));
    powerPointInformation.setProfilePictureLocation(baseUser.getProfilePictureLocation());
    powerPointInformation.setCompany(company);
    powerPointInformation.setLanguage(language);
    powerPointInformation.setName(baseUser.getFullName());
    powerPointInformation.setEmail(baseUser.getEmail());
    powerPointInformation.setPhone(baseUser.getPhoneNumber());
    resume.getOptionalRole().ifPresent(role -> powerPointInformation.setRole(role.getLabel()));
    powerPointInformation.setTitle(resume.getTitle());
    powerPointInformation.setSkills(resume.getSkills().stream().map(this::convertSkillToPowerPointSkill).toList());
    powerPointInformation
        .setIndustries(resume.getIndustries().stream().map((industry) -> industry.getLabel()).toList());
    powerPointInformation.setBackground(resume.getBackground());
    powerPointInformation
        .setExperiences(resume.getExperiences().stream().map(this::converExperienceToPowerPointExperience).toList());

    return powerPointInformation;
  }

  public Resource getFirstSlideAsImage(XMLSlideShow ppt) {
    XSLFSlide slide = ppt.getSlides().get(0);
    Dimension pgsize = ppt.getPageSize();

    // Scale factor for higher resolution
    double scale = 3.0; // Increase this factor for higher resolution

    int width = (int) (pgsize.width * scale);
    int height = (int) (pgsize.height * scale);

    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = img.createGraphics();

    // Apply scaling
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.scale(scale, scale);

    var font = graphics.getFont();

    graphics.setPaint(java.awt.Color.white);
    graphics.fill(new java.awt.Rectangle(0, 0, width, height));
    
    slide.draw(graphics);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ImageIO.write(img, "png", baos);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return new ByteArrayResource(baos.toByteArray());
  }

  private PowerPointInformation.PowerPointExperience converExperienceToPowerPointExperience(
      ResumeExperience experience) {
    PowerPointInformation.PowerPointExperience powerPointExperience = new PowerPointInformation.PowerPointExperience();
    powerPointExperience.setTitle(experience.getBasedOf().get().getTitle());
    if (experience.getBasedOf().get().getOccupation() != null) {
      powerPointExperience.setPosition(experience.getBasedOf().get().getOccupation().getLabel());
    }
    powerPointExperience.setDescriptions(experience.getDescriptions());
    return powerPointExperience;
  }

  private PowerPointInformation.PowerPointSkill convertSkillToPowerPointSkill(ResumeSkill skill) {
    PowerPointInformation.PowerPointSkill powerPointSkill = new PowerPointInformation.PowerPointSkill();
    skill.getParent().ifPresent(parent -> powerPointSkill.setParentSkill(parent.getLabel()));
    powerPointSkill.setChildSkills(skill.getSkills().stream().map((childSkill) -> childSkill.getLabel()).toList());
    return powerPointSkill;
  }

  public XMLSlideShow createPowerPointFromTemplate(PowerPointInformation pointInformation) {
    try (FileInputStream fis = new FileInputStream("src/main/resources/template.pptx")) {
      XMLSlideShow slideShow = new XMLSlideShow(fis);
      int slideToRemove = pointInformation.getLanguage().equals("en") ? 1 : 0;
      // XSLFSlideLayout relevantLayout = slideShow.findLayout(layoutToFind); At some point we will enable this
      slideShow.removeSlide(slideToRemove);
        var slide = slideShow.getSlides().get(0);
        for (var shape : slide.getShapes()) {
          FieldHandler
              .findByLabel(shape.getShapeName())
              .ifPresent((handler) -> handler.handleShape(shape, pointInformation));
        }
      return slideShow;
    } catch (IOException e) {
      return null;
    }
  }

}
