package com.reply.skillshub.controllers.resume.powerpoint;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.sl.draw.Drawable;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.reply.skillshub.data.resume.ShortCv;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import com.reply.skillshub.data.user.BaseUser;

@Service
public class PowerPointService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PowerPointService.class);

  public PowerPointInformation createPowerPointDto(BaseUser baseUser, ShortCv resume, String language, String company) {
    return createPowerPointDto(baseUser, resume, language, company, false);
  }

  public PowerPointInformation createPowerPointDto(BaseUser baseUser, ShortCv resume, String language, String company,
      boolean anonymous) {
    PowerPointInformation powerPointInformation = new PowerPointInformation();

    if (resume == null) {
      return powerPointInformation;
    }

    powerPointInformation.setPosition(resume.getRole());
    powerPointInformation.setProfilePictureLocation(baseUser.getProfilePictureLocation());
    powerPointInformation.setCompany(company);
    powerPointInformation.setLanguage(language);
    powerPointInformation.setName(baseUser.getFullName());
    powerPointInformation.setEmail(baseUser.getEmail());
    powerPointInformation.setPhone(baseUser.getPhoneNumber());
    powerPointInformation.setRole(resume.getRole());
    powerPointInformation.setTitle(resume.getTitle());
    powerPointInformation.setSkills(resume.getSkills().stream().map(this::convertSkillToPowerPointSkill).toList());
    powerPointInformation.setIndustries(resume.getIndustries());
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
    // default rendering options
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    graphics.setRenderingHint(Drawable.BUFFERED_IMAGE, new WeakReference<>(img));

    graphics.scale(scale, scale);

    slide.draw(graphics);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ImageIO.write(img, "png", baos);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    img.flush();
    graphics.dispose();
    return new ByteArrayResource(baos.toByteArray());
  }

  private PowerPointInformation.PowerPointExperience converExperienceToPowerPointExperience(
      ResumeExperience experience) {
    PowerPointInformation.PowerPointExperience powerPointExperience = new PowerPointInformation.PowerPointExperience();
    powerPointExperience.setTitle(experience.getTitle());
    powerPointExperience.setPosition(experience.getRole());
    powerPointExperience.setDescriptions(experience.getDescriptions());
    return powerPointExperience;
  }

  private PowerPointInformation.PowerPointSkill convertSkillToPowerPointSkill(ResumeSkill skill) {
    PowerPointInformation.PowerPointSkill powerPointSkill = new PowerPointInformation.PowerPointSkill();
    powerPointSkill.setParentSkill(skill.getParent());
    powerPointSkill.setChildSkills(skill.getSkills());
    return powerPointSkill;
  }

  public XMLSlideShow createSlideShowFromMultipleTemplates(List<PowerPointInformation> powerPointInformationList) {
    XMLSlideShow ppt = createPowerPointFromTemplate(powerPointInformationList.get(0));
    if (powerPointInformationList.size() == 1) {
      return ppt;
    }

    if (powerPointInformationList.size() > 1) {
      for (int count = 1; count < powerPointInformationList.size(); count++) {
        var slideShow = createPowerPointFromTemplate(powerPointInformationList.get(count));
        var createdSlide = slideShow.getSlides().get(0);
        ppt.createSlide().importContent(createdSlide);
        try {
          slideShow.close();
        } catch (IOException e) {
          logger.error("An IO Exception has been thrown on closing the slideshow", e);
        }
      }    
    }

    return ppt;
  }

  public XMLSlideShow createPowerPointFromTemplate(PowerPointInformation pointInformation) {
    var language = pointInformation.getLanguage();
    if (language == null || language.isEmpty()) {
      language = "en";
    }
    String templateName = String.format("template_%s.pptx", language);
    
    ClassPathResource resource = new ClassPathResource(templateName);
    try (var fis = resource.getInputStream()) {
      XMLSlideShow slideShow = new XMLSlideShow(fis);
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
