package com.reply.skillshub.controllers.resume.powerpoint;

import java.awt.Color;

import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import static com.reply.skillshub.controllers.resume.powerpoint.SharedValues.*;

public class ProfessionalBackground {

  private PowerPointInformation pointInformation;

  public ProfessionalBackground(PowerPointInformation pointInformation) {
    this.pointInformation = pointInformation;
  }

  public void addShapes(XSLFSlide slide) {
    this.addProfessionalBackgroundBackground(slide);
    this.addProfessionalBackground(slide);
    this.addProfessionalbackgroundTitle(slide);
  }

  private void addProfessionalbackgroundTitle(XSLFSlide slide) {
    XSLFTextShape title = slide.createTextBox();
    XSLFTextRun text = title.setText(getProfessionalBackgroundLabel());
    text.setFontSize(13.0);
    text.setFontFamily("PT Sans Narrow");
    text.setFontColor(Color.decode(TITLE_FONT));
    title.setFillColor(Color.decode(getTitleBackgroundColor(this.pointInformation.getCompany())));
    Util.setSizeAndPosition(title, START_FIRST_COLUMN, START_SECOND_ROW, WIDTH_FIRST_COLUMN, HEIGHT_TITLE_SHAPE);
  }

  private void addProfessionalBackgroundBackground(XSLFSlide slide) {
    XSLFTextShape background = slide.createTextBox();
    Util.setSizeAndPosition(background, START_FIRST_COLUMN, START_SECOND_ROW + HEIGHT_TITLE_SHAPE, WIDTH_FIRST_COLUMN,
        8.3);
    background.setFillColor(Color.decode(getTextCompanyColor(this.pointInformation.getCompany())));
  }

  private void addProfessionalBackground(XSLFSlide slide) {
    XSLFTextShape text = slide.createTextBox();
    text.clearText();
    XSLFTextParagraph p = text.addNewTextParagraph();
    var background = this.pointInformation.getBackground() == null ? "" : this.pointInformation.getBackground();
    XSLFTextRun run = p.addNewTextRun();
    run.setText(background);
    run.setFontSize(9.0);
    run.setFontFamily(TEXT_FONT_FACE);
    run.setFontColor(Color.decode("#252625"));
    text.setVerticalAlignment(VerticalAlignment.TOP);
    Util.setSizeAndPosition(text, START_FIRST_COLUMN, START_SECOND_ROW + HEIGHT_TITLE_SHAPE, WIDTH_FIRST_COLUMN, 8.3);
  }

  private String getProfessionalBackgroundLabel() {
    if (this.pointInformation.getLanguage() == null) {
      return "Professional Background";
    }
    switch (this.pointInformation.getLanguage()) {
      case "en":
        return "Professional Background";
      case "de":
        return "Berufliche Erfahrungen";
      default:
        return "Professional Background";
    }
  }

}
