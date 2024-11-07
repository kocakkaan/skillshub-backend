package com.reply.skillshub.controllers.resume.powerpoint;

import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.reply.skillshub.controllers.resume.powerpoint.PowerPointInformation.PowerPointSkill;

import static com.reply.skillshub.controllers.resume.powerpoint.SharedValues.*;

import java.awt.Color;
import java.util.List;

public class FunctionalExperience {

    private PowerPointInformation pointInformation;

    public FunctionalExperience(PowerPointInformation pointInformation) {
        this.pointInformation = pointInformation;
    }

    public void addShapes(XSLFSlide slide) {
        this.addFunctionExperienceBackground(slide);
        this.addFunctionalExperienceText(slide);
        this.addFunctionalExperienceTitle(slide);
    }

    private void addFunctionalExperienceTitle(XSLFSlide slide) {
        XSLFTextShape title = slide.createTextBox();
        XSLFTextRun text = title.setText(getFunctionalExpertiseLabel());
        text.setFontSize(13.0);
        text.setFontFamily(TITLE_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        title.setFillColor(Color.decode(getTitleBackgroundColor(this.pointInformation.getCompany())));
        Util.setSizeAndPosition(title, 11, STARTING_VERTICAL_POINT_FIRST_ROW_SHAPES, WIDTH_EXPERTISE, HEIGHT_TITLE_SHAPE);
    }
    
    private void addFunctionExperienceBackground(XSLFSlide slide) {
        XSLFTextShape background = slide.createAutoShape();
        Util.setSizeAndPosition(background, 11, STARTING_VERTICAL_POINT_FIRST_ROW_SHAPES + HEIGHT_TITLE_SHAPE, WIDTH_EXPERTISE, HEIGHT_EXPERTISE_BACKGROUND);
        background.setFillColor(Color.decode(getTextCompanyColor(this.pointInformation.getCompany())));
    }

    private void addFunctionalExperienceText(XSLFSlide slide) {
        XSLFTextShape text = slide.createTextBox();
        text.clearText();

        for (var pptSkill : this.pointInformation.getSkills()) {
          XSLFTextParagraph  run = text.addNewTextParagraph();
          run.setBullet(true);
          run.setIndent(10.0);
          run.setBulletCharacter("\u25AA");
          var test = run.addNewTextRun();
          test.setText(getStringFromSkill(pptSkill));
          test.setFontSize(8.0);
          test.setFontFamily(TEXT_FONT_FACE);
        }
        Util.setSizeAndPosition(text, 11, 4.5, WIDTH_EXPERTISE, HEIGHT_EXPERTISE_BACKGROUND);
    }

    private String getStringFromSkill(PowerPointSkill skill) {
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

    private String getFunctionalExpertiseLabel() {
        switch (this.pointInformation.getLanguage()) {
          case "en":
            return "Functional Expertise";
          case "de":
            return "Funktionale Expertise";
          default:
            return "Functional Expertise";
        }
      }
}
