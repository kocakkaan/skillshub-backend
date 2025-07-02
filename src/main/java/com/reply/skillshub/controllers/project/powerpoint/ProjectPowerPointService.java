package com.reply.skillshub.controllers.project.powerpoint;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.sl.draw.Drawable;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.reply.skillshub.data.project.Project;

@Service
public class ProjectPowerPointService {

  @Value("${skillhub.projectpicture.path}")
  private String projectPicturePath;

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProjectPowerPointService.class);

  public PowerPointInformation createPowerPointDto(Project resume, String language) {
    PowerPointInformation powerPointInformation = new PowerPointInformation();

    if (resume == null) {
      return powerPointInformation;
    }
    powerPointInformation.setProjectId(resume.getFormattedProjectId());
    resume.getReferenceByLanguage(language)
        .ifPresent(reference -> {
          powerPointInformation.setProjectTitle(reference.getTitle());
          powerPointInformation.setApproachTechnologies(reference.getApproachTechnologies());
          powerPointInformation.setInitialSituation(reference.getInitialSituation());
          powerPointInformation.setChallenges(reference.getChallenges());
          powerPointInformation.setValueAddedText0(reference.getValueAddedText0());
          powerPointInformation.setValueAddedText1(reference.getValueAddedText1());
          powerPointInformation.setValueAddedText2(reference.getValueAddedText2());
        });


    if (resume.getProjectPictureLocation() != null && !resume.getProjectPictureLocation().isEmpty()) {
      String fullPath = Paths.get(projectPicturePath, resume.getProjectPictureLocation()).toString();
      powerPointInformation.setProjectPictureLocation(fullPath);
    } else {
      powerPointInformation.setProjectPictureLocation(null);
    }

    powerPointInformation.setLanguage(language);

    return powerPointInformation;
  }

  public XMLSlideShow createPowerPointFromTemplate(PowerPointInformation pointInformation) {
    var language = pointInformation.getLanguage();
    if (language == null || language.isEmpty()) {
      language = "en";
    }
    String templateName = String.format("templates/projectreferences/project_template_%s.pptx", language);

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

}
