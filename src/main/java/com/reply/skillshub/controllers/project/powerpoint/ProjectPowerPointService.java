package com.reply.skillshub.controllers.project.powerpoint;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
      fis.close();
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
    if (powerPointInformationList.size() == 1) {
      return createPowerPointFromTemplate(powerPointInformationList.get(0));
    }

    var uniqueFolder = UUID.randomUUID().toString();
    String uniqueFolderPath = String.format("temp/%s", uniqueFolder);
    ensureFolderExists(uniqueFolderPath);

    if (powerPointInformationList.size() > 1) {
      for (int count = 0; count < powerPointInformationList.size(); count++) {
        var slideShow = createPowerPointFromTemplate(powerPointInformationList.get(count));

        String templateName = String.format("%s/output_%s.pptx", uniqueFolderPath, count);
        try (FileOutputStream out = new FileOutputStream(templateName)) {
          slideShow.write(out);
          out.flush();
          slideShow.close();
        } catch (IOException e) {
          logger.error("Error while writing PowerPoint to output file", e);
        }
      }
    }
    return mergePowerPointsFromFolder(uniqueFolderPath);
  }

  private static void ensureFolderExists(String folderPath) {
    try {
      Path folder = Paths.get(folderPath);
      if (!Files.exists(folder)) {
        Files.createDirectories(folder);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error creating folder: " + folderPath, e);
    }
  }

  public static XMLSlideShow mergePowerPointsFromFolder(String folderPath) {
    // Load all PowerPoint files from the folder
    try {
      List<XMLSlideShow> presentations = loadPresentationsFromFolder(folderPath);

      // Merge the presentations
      XMLSlideShow mergedPpt = mergePresentations(presentations);

      // Delete all files in the folder
      deleteFilesInFolder(folderPath);

      return mergedPpt;
    } catch (IOException e) {
      throw new RuntimeException("Error merging PowerPoint presentations from folder: " + folderPath, e);
    }

  }

  private static List<XMLSlideShow> loadPresentationsFromFolder(String folderPath) throws IOException {
    List<XMLSlideShow> presentations = new ArrayList<>();
    Path folder = Paths.get(folderPath);

    // Iterate over each file in the folder
    DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.{pptx}");
    for (Path file : stream) {
      try (FileInputStream fis = new FileInputStream(file.toFile())) {
        XMLSlideShow ppt = new XMLSlideShow(fis);
        presentations.add(ppt);
      }
    }
    stream.close();
    return presentations;
  }

  private static XMLSlideShow mergePresentations(List<XMLSlideShow> presentations) {
    XMLSlideShow ppt = null;
    for (int i = 0; i < presentations.size(); i++) {
      if (i == 0) {
        ppt = presentations.get(i);
      } else {
        for (XSLFSlide srcSlide : presentations.get(i).getSlides()) {
          XSLFSlide newSlide = ppt.createSlide();
          newSlide.importContent(srcSlide);
        }
        try {
          presentations.get(i).close();

        } catch (Exception e) {
          // TODO: handle exception
        }

      }
    }
    return ppt;
  }

  private static void deleteFilesInFolder(String folderPath) throws IOException {
    Path folder = Paths.get(folderPath);
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
      for (Path file : stream) {
        Files.delete(file);
      }
    } catch (IOException e) {
      logger.error("Error deleting files in folder: " + folderPath, e);
    }
    Files.delete(folder);
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
