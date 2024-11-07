package com.reply.skillshub.controllers.resume.powerpoint;

import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import static com.reply.skillshub.controllers.resume.powerpoint.SharedValues.*;

import java.awt.Color;

public class IndustryExperience {

  private PowerPointInformation pointInformation;

  public IndustryExperience(PowerPointInformation pointInformation) {
    this.pointInformation = pointInformation;
  }

  public void addShapes(XSLFSlide slide) {
    this.addIndustryExperienceBackgroundShape(slide);
    this.addIndustryExperienceText(slide);
    this.addTitleIndustryExperience(slide);
  }

    private void addTitleIndustryExperience(XSLFSlide slide) {
        XSLFTextShape title = slide.createTextBox();
        XSLFTextRun text = title.setText(getIndustryExperienceLabel());
        text.setFontSize(13.0);
        text.setFontFamily(TITLE_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        title.setFillColor(Color.decode(getTitleBackgroundColor(this.pointInformation.getCompany())));
        Util.setSizeAndPosition(title, START_SECOND_COLUMN, STARTING_VERTICAL_POINT_FIRST_ROW_SHAPES, WIDTH_EXPERTISE, 0.68);
    }

    private void addIndustryExperienceBackgroundShape(XSLFSlide slide) {
        XSLFTextShape background = slide.createAutoShape();
        Util.setSizeAndPosition(background, START_SECOND_COLUMN, STARTING_VERTICAL_POINT_FIRST_ROW_SHAPES + HEIGHT_TITLE_SHAPE, WIDTH_EXPERTISE, HEIGHT_EXPERTISE_BACKGROUND);
        background.setFillColor(Color.decode(getTextCompanyColor(this.pointInformation.getCompany())));
    }

    private void addIndustryExperienceText(XSLFSlide slide) {
        XSLFTextShape text = slide.createTextBox();
        text.clearText();

        for (String industry : this.pointInformation.getIndustries()) {
          XSLFTextParagraph  run = text.addNewTextParagraph();
          run.setBullet(true);
          run.setIndent(10.0);
          run.setBulletCharacter("\u25AA");
          var test = run.addNewTextRun();
          test.setText(industry);
          test.setFontSize(8.0);
          test.setFontFamily(TEXT_FONT_FACE);
        }

        Util.setSizeAndPosition(text, START_SECOND_COLUMN, 4.5, WIDTH_EXPERTISE, HEIGHT_EXPERTISE_TEXT);
    }
    
    private String getIndustryExperienceLabel() {
        switch (this.pointInformation.getLanguage()) {
          case "en":
            return "Industry Experience";
          case "de":
            return "IndustrieErfahrung";
          default:
            return "Industry Experience";
        }
      }
}
