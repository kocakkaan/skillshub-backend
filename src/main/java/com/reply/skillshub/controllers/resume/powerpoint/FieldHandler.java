package com.reply.skillshub.controllers.resume.powerpoint;

import java.util.Optional;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.sl.usermodel.AutoNumberingScheme;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.reply.skillshub.base.services.images.FileTypeDetector;
import com.reply.skillshub.base.services.images.ResizeImageService;
import com.reply.skillshub.controllers.resume.powerpoint.PowerPointInformation.PowerPointSkill;

import lombok.Data;

public enum FieldHandler {
  PROFILE_PICTURE("ProfilePicture") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFPictureShape) {
        InputStream runStream = loadFile(powerPointInformation.getProfilePictureLocation());
        if (runStream == null) {
          return;
        }
        XSLFPictureShape pictureShape = (XSLFPictureShape) shape;
        var pictureData = pictureShape.getPictureData();
        var dimensionPixels = pictureData.getImageDimensionInPixels();
        String fileType = FileTypeDetector.detectFileType(powerPointInformation.getProfilePictureLocation()).orElse("PNG");
        byte[] resizedImageData = ResizeImageService.resizeImage(runStream, (int) dimensionPixels.getWidth() * 4, (int) dimensionPixels.getHeight() * 4, fileType);
        try {
          pictureData.setData(resizedImageData);
        } catch (Exception e) {
          // TODO: handle exception
        }

      }
    }
  },
  INDUSTRY("IndustryExpertiseText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        var paragraph = text_shape.getTextParagraphs().get(0);
        var paragraphStyles = getParagraphStyles(paragraph);

        var textRun = paragraph.getTextRuns().get(0);
        var textStyles = getTextRunStyles(textRun);

        text_shape.clearText();

        for (var industry : powerPointInformation.getIndustries()) {
          XSLFTextParagraph run = text_shape.addNewTextParagraph();
          setParagraphStyles(paragraphStyles, run);

          var test = run.addNewTextRun();
          test.setText(industry);
          setTextRunStyles(textStyles, test);
        }
      }
    }
  },
  FUNCTION_EXPERTISE("FunctionExpertiseText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        var paragraph = text_shape.getTextParagraphs().get(0);
        var styles = getParagraphStyles(paragraph);

        var textRun = paragraph.getTextRuns().get(0);
        var textStyles = getTextRunStyles(textRun);

        text_shape.clearText();

        for (var pptSkill : powerPointInformation.getSkills()) {
          XSLFTextParagraph run = text_shape.addNewTextParagraph();
          setParagraphStyles(styles, run);

          var test = run.addNewTextRun();
          test.setText(getStringFromSkill(pptSkill));
          setTextRunStyles(textStyles, test);
        }
      }
    }
  },
  PROFESSIONAL_BACKGROUND("ProfessionalBackgroundText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getBackground()));
      }
    }
  },
  NAME_TEXT("NameText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getName()));
      }
    }
  },
  ROLE_TEXT("RoleText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getRole()));
      }
    }
  },
  EMAIL_TEXT("emailText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getEmail()));
      }
    }
  },
  PHONE_TEXT("phoneText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getPhone()));
      }
    }
  },
  PROJECT_TEXT("ProjectText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;

        var paragraphs = text_shape.getTextParagraphs();

        var titleParagraph = paragraphs.get(0);
        var titleStyle = getParagraphStyles(titleParagraph);
        var titleTextStyle = getTextRunStyles(titleParagraph.getTextRuns().get(0));

        var firstDescriptionParagraph = paragraphs.get(1);
        var descriptionStyle = getParagraphStyles(firstDescriptionParagraph);
        var descriptionTextStyle = getTextRunStyles(firstDescriptionParagraph.getTextRuns().get(0));

        text_shape.clearText();

        for (var experience : powerPointInformation.getExperiences()) {
          var newTitleParagraph = text_shape.addNewTextParagraph();
          setParagraphStyles(titleStyle, newTitleParagraph);
          System.out.println(newTitleParagraph.getBulletCharacter());

          var titleText = newTitleParagraph.addNewTextRun();
          setTextRunStyles(titleTextStyle, titleText);
          titleText.setText(experience.getTitle() + " - " + experience.getPosition());

          for (var description : experience.getDescriptions()) {

            var descriptionParagraph = text_shape.addNewTextParagraph();
            setParagraphStyles(descriptionStyle, descriptionParagraph);

            var textDescription = descriptionParagraph.addNewTextRun();
            setTextRunStyles(descriptionTextStyle, textDescription);
            textDescription.setText(description);
          }
        }
      }
    }
  };

  FieldHandler(String label) {
    this.fieldLabel = label;
  }

  public String fieldLabel;

  public abstract void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation);

  public static Optional<FieldHandler> findByLabel(String label) {
    for (FieldHandler handler : FieldHandler.values()) {
      if (handler.fieldLabel.equals(label)) {
        return Optional.of(handler);
      }
    }
    return Optional.empty();
  }

  private static InputStream loadFile(String path) {
    if (path == null) {
      return null;
    }
    try {
      InputStream inputStream = new FileInputStream(path);
      return inputStream;
    } catch (IOException e) {
      return null;
    }
  }

  private static String getString(String string) {
    return string == null ? "" : string;
  }

  private static String getStringFromSkill(PowerPointSkill skill) {
    StringBuilder sb = new StringBuilder();
    sb.append(skill.getParentSkill());
    sb.append(" (");
    for (var i = 0; i < skill.getChildSkills().size(); i++) {
      if (i > 0 && i < skill.getChildSkills().size()) {
        sb.append(", ");
      }
      sb.append(skill.getChildSkills().get(i));
    }
    sb.append(")");
    return sb.toString();
  }

  private static void setParagraphStyles(ParagraphStyles styles, XSLFTextParagraph paragraph) {
    if (styles.getBulletStyle() != null) {
      paragraph.setBullet(true);
      var bullet = styles.getBulletStyle();
      bullet.getBulletCharacter().ifPresent((value) -> paragraph.setBulletCharacter(value));
      bullet.getBulletFont().ifPresent((value) -> paragraph.setBulletFont(value));
      bullet.getBulletFontColor().ifPresent((value) -> paragraph.setBulletFontColor(value));
      bullet.getBulletFontSize().ifPresent((value) -> paragraph.setBulletFontSize(value));
    }
    paragraph.setIndent(styles.getIndent());
    paragraph.setIndentLevel(styles.getIndentLevel());
    paragraph.setLeftMargin(styles.getLeftMargin());
    paragraph.setSpaceBefore(styles.getSpaceBefore());
    paragraph.setSpaceAfter(styles.getSpaceAfter());
    paragraph.setLineSpacing(styles.getLineSpacing());
  }

  private static ParagraphStyles getParagraphStyles(XSLFTextParagraph paragraph) {
    var styles = new ParagraphStyles();
    if (paragraph.getBulletStyle() != null) {
      var bullet = new ParagraphStyles.BulletStyle();
      bullet.setAutoNumberingScheme(Optional.ofNullable(paragraph.getAutoNumberingScheme()));
      bullet.setAutoNumberingStartAt(Optional.ofNullable(paragraph.getAutoNumberingStartAt()));
      bullet.setBulletCharacter(Optional.ofNullable(paragraph.getBulletCharacter()));
      bullet.setBulletFontColor(Optional.ofNullable(paragraph.getBulletFontColor()));
      bullet.setBulletFontSize(Optional.ofNullable(paragraph.getBulletFontSize()));
      bullet.setBulletFont(Optional.ofNullable(paragraph.getBulletFont()));
      styles.setBulletStyle(bullet);
    }
    styles.setIndent(paragraph.getIndent());
    styles.setIndentLevel(paragraph.getIndentLevel());
    styles.setSpaceAfter(paragraph.getSpaceAfter());
    styles.setSpaceBefore(paragraph.getSpaceBefore());
    styles.setLeftMargin(paragraph.getLeftMargin());
    styles.setLineSpacing(paragraph.getLineSpacing());
    return styles;
  }

  private static TextRunStyles getTextRunStyles(XSLFTextRun text) {
    var styles = new TextRunStyles();
    styles.setFontFamily(text.getFontFamily());
    styles.setFontSize(text.getFontSize());
    styles.setFontColor(text.getFontColor());
    styles.setBold(text.isBold());
    return styles;
  }

  private static void setTextRunStyles(TextRunStyles styles, XSLFTextRun text) {
    text.setFontFamily(styles.getFontFamily());
    text.setFontColor(styles.getFontColor());
    text.setFontSize(styles.getFontSize());
    text.setBold(styles.getBold());
  }

  @Data
  private static class ParagraphStyles {
    private Double leftMargin;
    private int indentLevel;
    private Double indent;
    private BulletStyle bulletStyle;
    private Double spaceBefore;
    private Double spaceAfter;
    private Double lineSpacing;

    @Data
    static class BulletStyle {
      Optional<String> bulletCharacter;
      Optional<String> bulletFont;
      Optional<Double> bulletFontSize;
      Optional<PaintStyle> bulletFontColor;
      Optional<AutoNumberingScheme> autoNumberingScheme;
      Optional<Integer> autoNumberingStartAt;
    }
  }

  @Data
  private static class TextRunStyles {
    private Double fontSize;
    private String fontFamily;
    private PaintStyle fontColor;
    private Boolean bold;
  }
}
