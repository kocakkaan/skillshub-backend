package com.reply.skillshub.controllers.resume.powerpoint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.reply.skillshub.data.resume.Resume;
import com.reply.skillshub.data.resumeexperience.ResumeExperience;
import com.reply.skillshub.data.resumeskill.ResumeSkill;
import static com.reply.skillshub.controllers.resume.powerpoint.SharedValues.*;

@Service
public class PowerPointService {

  
  public PowerPointInformation createPowerPointDto(Resume resume, String language, String company) {
    return createPowerPointDto(resume, language, company, false) ;
}

    public PowerPointInformation createPowerPointDto(Resume resume, String language, String company, boolean anonymous) {
        PowerPointInformation powerPointInformation = new PowerPointInformation();

        if (resume == null) {
            return powerPointInformation;
        }

        powerPointInformation.setPosition(resume.getRole().getLabel());
        powerPointInformation.setProfilePictureLocation(resume.getUser().getProfilePictureLocation());
        powerPointInformation.setCompany(company);
        powerPointInformation.setLanguage(language);
        powerPointInformation.setName(resume.getUser().getFullname());
        powerPointInformation.setEmail(resume.getUser().getEmail());
        powerPointInformation.setPhone(resume.getUser().getPhoneNumber());
        powerPointInformation.setRole(resume.getRole().getLabel());
        powerPointInformation.setTitle(resume.getTitle());
        powerPointInformation.setSkills(resume.getSkills().stream().map(this::convertSkillToPowerPointSkill).toList());
        powerPointInformation.setIndustries(resume.getIndustries().stream().map((industry) -> industry.getLabel()).toList());
        powerPointInformation.setBackground(resume.getBackground());
        powerPointInformation.setExperiences(resume.getExperiences().stream().map(this::converExperienceToPowerPointExperience).toList());

        return powerPointInformation;
    }

    public Resource getFirstSlideAsImage(XMLSlideShow ppt) {
          XSLFSlide slide = ppt.getSlides().get(0);
          Dimension pgsize = ppt.getPageSize();

          BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
          Graphics2D graphics = img.createGraphics();
          graphics.setPaint(java.awt.Color.white);
          graphics.fill(new java.awt.Rectangle(0, 0, pgsize.width, pgsize.height));
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
    

    private PowerPointInformation.PowerPointExperience converExperienceToPowerPointExperience(ResumeExperience experience) {
      PowerPointInformation.PowerPointExperience powerPointExperience = new PowerPointInformation.PowerPointExperience();
      powerPointExperience.setTitle(experience.getBasedOf().get().getTitle());
      powerPointExperience.setPosition(experience.getBasedOf().get().getOccupation().get(0).getLabel());
      powerPointExperience.setDescriptions(experience.getDescriptions());
      return powerPointExperience;
    }

    private PowerPointInformation.PowerPointSkill convertSkillToPowerPointSkill(ResumeSkill skill) {
      PowerPointInformation.PowerPointSkill powerPointSkill = new PowerPointInformation.PowerPointSkill();
      powerPointSkill.setParentSkill(skill.getParent().getLabel());
      powerPointSkill.setChildSkills(skill.getSkills().stream().map((childSkill) -> childSkill.getLabel()).toList());
      return powerPointSkill;
    }

    public XMLSlideShow createPowerPoint(PowerPointInformation pointInformation) {
        XMLSlideShow ppt = new XMLSlideShow();
            
        ppt.setPageSize(new Dimension(16 * 60, 9 * 60));

        XSLFSlide slide = ppt.createSlide();
        
        addSlideTitle(slide);
        addLogo(ppt, slide, pointInformation);
        addFootText(slide);
        new BasicInfo(ppt, pointInformation).addShapes(slide);
        new FunctionalExperience(pointInformation).addShapes(slide);
        new IndustryExperience(pointInformation).addShapes(slide);
        new ProfessionalBackground(pointInformation).addShapes(slide);
        new ProjectExperience(pointInformation).addShapes(slide);

        return ppt;
    }


    private PictureType getPictureDataFromString(String type) {
      switch (type) {
        case "PNG":
          return PictureData.PictureType.PNG;
        case "jpg":
        case "jpeg":
          return PictureData.PictureType.JPEG;
        default:
          return PictureData.PictureType.PNG; // TODO Do I really want to do it like this
      }

    }

    
    private void addSlideTitle(XSLFSlide slide) {
        XSLFTextShape title = slide.createTextBox();
        XSLFTextRun text = title.setText(getSlideTitle("en"));
        text.setFontSize(28.0);
        text.setFontFamily(TITLE_FONT_FACE);
        text.setFontColor(Color.decode("#00444C"));
        text.setBold(true);
        Util.setSizeAndPosition(title, 1.01, 1.11, 27.61, 1.45);
    }

    private String getSlideTitle(String language) {
        switch (language) {
          case "en":
            return "SHORT SUMMARY AND PROJECT EXPERIENCE";
          case "de":
            return "PROFILÜBERSICHT UND PROJEKTERFAHRUNG";
          default:
            return "SHORT SUMMARY AND PROJECT EXPERIENCE";
        }
      }

    private void addLogo(XMLSlideShow ppt, XSLFSlide slide, PowerPointInformation pointInformation) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
              
        InputStream is = classloader.getResourceAsStream("static/logo/ml_reply_logo_white.png");
        try {
          XSLFPictureData test = ppt.addPicture(is.readAllBytes(), PictureData.PictureType.PNG);
          XSLFPictureShape logo = slide.createPicture(test);
          Util.setSizeAndPosition(logo,  28.63, 0, 5.24, 2.62);
          logo.setFillColor(Color.decode(getTitleBackgroundColor(pointInformation.getCompany())));
        } catch (IOException e) {
          
        }
    }

    private void addFootText(XSLFSlide slide) {
        XSLFTextShape text = slide.createTextBox();
        XSLFTextRun run = text.setText("This is a footer");
        run.setFontSize(12.0);
        run.setFontFamily(SharedValues.TEXT_FONT_FACE);
        run.setFontColor(Color.decode("#7F7F7F"));
        text.setVerticalAlignment(VerticalAlignment.TOP);
        Util.setSizeAndPosition(text, 0.8, 17.88, 7.4, 0.77);
    }
}
