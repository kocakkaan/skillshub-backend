package com.reply.skillshub.controllers.resume.powerpoint;

import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import static com.reply.skillshub.controllers.resume.powerpoint.SharedValues.*;

import java.awt.Color;

public class ProjectExperience {

    private PowerPointInformation pointInformation;

    public ProjectExperience(PowerPointInformation pointInformation) {
        this.pointInformation = pointInformation;
    }

    public void addShapes(XSLFSlide slide) {
        this.addProjectExperienceBackground(slide);
        this.addProjectExperience(slide);
        this.addProjectExperienceTitle(slide);
    }
    
    private void addProjectExperience(XSLFSlide slide) {
        XSLFTextShape title = slide.createTextBox();
        title.clearText();

        for (var experience : this.pointInformation.getExperiences()) {
            var paragraph = title.addNewTextParagraph();
            var titleText = paragraph.addNewTextRun();
            titleText.setText(experience.getTitle() + " - " + experience.getPosition());
            titleText.setFontSize(8.0);
            titleText.setBold(true);
            for (var description : experience.getDescriptions()) {
                var descriptionParagraph = title.addNewTextParagraph();
                descriptionParagraph.setBullet(true);
                descriptionParagraph.setIndent(4.0);
                descriptionParagraph.setIndentLevel(0);
                descriptionParagraph.setLeftMargin(8.0);
                descriptionParagraph.setBulletCharacter("\u25AA");
                var textDescription = descriptionParagraph.addNewTextRun();
                textDescription.setText(description);
                textDescription.setFontSize(8.0);
            }
        }
        Util.setSizeAndPosition(title, 11, START_SECOND_ROW + HEIGHT_TITLE_SHAPE, WIDTH_SECOND_COLUMN, 8.3);
    }

    private void addProjectExperienceTitle(XSLFSlide slide) {
        XSLFTextShape title = slide.createTextBox();
        XSLFTextRun text = title.setText(this.getRelevantProjectExperienceLabel());
        text.setFontSize(13.0);
        text.setFontFamily("PT Sans Narrow");
        text.setFontColor(Color.decode(TITLE_FONT));
        title.setFillColor(Color.decode(getTitleBackgroundColor(this.pointInformation.getCompany())));
        Util.setSizeAndPosition(title, 11, START_SECOND_ROW, WIDTH_SECOND_COLUMN, HEIGHT_TITLE_SHAPE);
    }

    private void addProjectExperienceBackground(XSLFSlide slide) {
        XSLFTextShape background = slide.createAutoShape();
        background.setFillColor(Color.decode(getTextCompanyColor(this.pointInformation.getCompany())));
        Util.setSizeAndPosition(background, 11, START_SECOND_ROW + HEIGHT_TITLE_SHAPE, WIDTH_SECOND_COLUMN, 8.3);
    }

    private String getRelevantProjectExperienceLabel() {
        if (this.pointInformation.getLanguage() == null) {
            return "Relevant Project Experience (Selection)";
        }
        switch (this.pointInformation.getLanguage()) {
          case "en":
            return "Relevant Project Experience (Selection)";
          case "de":
            return "Relevant Projekterfahrung (Auswahl)";
          default:
            return "Relevant Project Experience (Selection)";
        }
    }

}
