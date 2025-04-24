package com.reply.skillshub.controllers.project.powerpoint;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.poi.sl.usermodel.AutoNumberingScheme;
import org.apache.poi.sl.usermodel.PaintStyle;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import lombok.Data;

public enum FieldHandler {

  PROJECT_TITLE("ProjectTitle") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getProjectTitle()));
      }
    }
  },

  PROJECT_ID("ProjectID") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        var textTemplate = "%s – References";
        text_shape.setText(String.format(textTemplate, getString(powerPointInformation.getProjectId())));
      }
    }
  },

  INITIAL_SITUATION("InitialSituation") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        var paragraph = text_shape.getTextParagraphs().get(0);
        var paragraphStyles = getParagraphStyles(paragraph);

        var textRun = paragraph.getTextRuns().get(0);
        var textStyles = getTextRunStyles(textRun);

        text_shape.clearText();

        for (var initialSituation : powerPointInformation.getInitialSituation()) {
          XSLFTextParagraph run = text_shape.addNewTextParagraph();
          setParagraphStyles(paragraphStyles, run);

          var test = run.addNewTextRun();
          test.setText(initialSituation);
          setTextRunStyles(textStyles, test);
        }
      }
    }
  },
  APPROACH_TECHNOLOGIES("ApproachTechnologies") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        var paragraph = text_shape.getTextParagraphs().get(0);
        var paragraphStyles = getParagraphStyles(paragraph);

        var textRun = paragraph.getTextRuns().get(0);
        var textStyles = getTextRunStyles(textRun);

        text_shape.clearText();

        for (var industry : powerPointInformation.getApproachTechnologies()) {
          XSLFTextParagraph run = text_shape.addNewTextParagraph();
          setParagraphStyles(paragraphStyles, run);

          var test = run.addNewTextRun();
          test.setText(industry);
          setTextRunStyles(textStyles, test);
        }
      }
    }
  },
  DIAMOND_TEXT("DiamondText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getDiamondText()));
      }
    }
  },
  MONEY_TEXT("MoneyText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getMoneyText()));
      }
    }
  },
  GRAPH_TEXT("GraphText") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        text_shape.setText(getString(powerPointInformation.getGraphText()));
      }
    }
  },

  CHALLENGES("Challenges") {
    @Override
    public void handleShape(XSLFShape shape, PowerPointInformation powerPointInformation) {
      if (shape instanceof XSLFTextShape) {
        XSLFTextShape text_shape = (XSLFTextShape) shape;
        var paragraph = text_shape.getTextParagraphs().get(0);
        var paragraphStyles = getParagraphStyles(paragraph);

        var textRun = paragraph.getTextRuns().get(0);
        var textStyles = getTextRunStyles(textRun);

        text_shape.clearText();

        for (var industry : powerPointInformation.getChallenges()) {
          XSLFTextParagraph run = text_shape.addNewTextParagraph();
          setParagraphStyles(paragraphStyles, run);

          var test = run.addNewTextRun();
          test.setText(industry);
          setTextRunStyles(textStyles, test);
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
