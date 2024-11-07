package com.reply.skillshub.controllers.resume.powerpoint;

import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import com.reply.skillshub.base.services.images.FileTypeDetector;
import com.reply.skillshub.base.services.images.ResizeImageService;

import static com.reply.skillshub.controllers.resume.powerpoint.SharedValues.*;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class BasicInfo {

    private XMLSlideShow ppt;
    private PowerPointInformation pointInformation;

    public BasicInfo(XMLSlideShow ppt, PowerPointInformation pointInformation) {
        this.ppt = ppt;
        this.pointInformation = pointInformation;
    }

    public void addShapes(XSLFSlide slide) {
        this.addBasicInfoBackgroundShape(slide);
        this.addPositionField(slide);
        this.addNameField(slide);
        this.addCompanyField(slide);
        this.addEmailInput(slide);
        this.addEmailLabel(slide);
        this.addMobileField(slide);
        this.addMobileLabel(slide);
        this.addRoleField(slide);
        this.addProfilePicture(slide);
    }

    
    private void addProfilePicture(XSLFSlide slide) {
        double fixedWidthCm = 2.8;
        double fixedHeightCm = 4.05;
        int fixedWidthPixel = Util.convertCmToPixels(fixedWidthCm);
        int fixedHeightPixel = Util.convertCmToPixels(fixedHeightCm);
       
        InputStream run_stream = loadFile(this.pointInformation.getProfilePictureLocation());
        String fileType = FileTypeDetector.detectFileType(this.pointInformation.getProfilePictureLocation()).get();
        PictureData.PictureType type = getPictureDataFromString(fileType);
        byte[] resizedImageData = ResizeImageService.resizeImage(run_stream, fixedWidthPixel, fixedHeightPixel);
        XSLFPictureData picture = this.ppt.addPicture(resizedImageData, type);
        XSLFPictureShape logo = slide.createPicture(picture);
        Util.setSizeAndPosition(logo,  1.24, 3.8, fixedWidthCm, fixedHeightCm);
    }

    private InputStream loadFile(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            return inputStream;
        } catch (IOException e) {
            return null;
        }
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
    
    private void addBasicInfoBackgroundShape(XSLFSlide slide) {
        XSLFTextShape background = slide.createAutoShape();
        Util.setSizeAndPosition(background, START_FIRST_COLUMN, STARTING_VERTICAL_POINT_FIRST_ROW_SHAPES, WIDTH_FIRST_COLUMN, 4.55);
        background.setFillColor(Color.decode(getTitleBackgroundColor(this.pointInformation.getCompany())));

    }

    private void addCompanyField(XSLFSlide slide) {
        XSLFTextShape company = slide.createTextBox();
        XSLFTextRun text = company.setText(getString(this.pointInformation.getCompany()));
        text.setFontSize(10.0);
        text.setFontFamily(TEXT_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));

        Util.setSizeAndPosition(company, 4.62, 5.77, 5.46, 0.68);
    }

    private void addRoleField(XSLFSlide slide) {
        XSLFTextShape role = slide.createTextBox();
        XSLFTextRun text = role.setText("Role");
        text.setFontSize(10.0);
        text.setFontFamily(TEXT_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        Util.setSizeAndPosition(role, START_SECOND_COLUMN, 2.56, 5.5, 1.45);
    }

    private void addNameField(XSLFSlide slide) {
        XSLFTextShape role = slide.createTextBox();
        XSLFTextRun text = role.setText(getString(this.pointInformation.getName()));
        text.setFontSize(14.0);
        text.setFontFamily(TEXT_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        Util.setSizeAndPosition(role, 4.62, 3.8, 5.46, 0.81);
    }

    private void addPositionField(XSLFSlide slide) {
        XSLFTextShape role = slide.createTextBox();
        XSLFTextRun text = role.setText(getString(this.pointInformation.getPosition()));
        text.setFontSize(10.0);
        text.setFontFamily(TEXT_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        Util.setSizeAndPosition(role, 4.62, 5.05, 5.46, 0.6);
    }
    
    private void addMobileLabel(XSLFSlide slide) {
        XSLFTextShape mobileLabel = slide.createTextBox();
        XSLFTextRun text = mobileLabel.setText("Mobile");
        text.setFontSize(10.0);
        text.setFontFamily(TEXT_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        Util.setSizeAndPosition(mobileLabel, 4.62, 6.66, 2.02, 0.6);
    }

    private void addMobileField(XSLFSlide slide) {
        XSLFTextShape mobileField = slide.createTextBox();
        XSLFTextRun text = mobileField.setText(getString(this.pointInformation.getPhone()));
        text.setFontSize(10.0);
        text.setFontFamily(TEXT_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        Util.setSizeAndPosition(mobileField, 6.15, 6.66, 3.95, 0.6);
    }

    private void addEmailLabel(XSLFSlide slide) {
        XSLFTextShape emailLabel = slide.createTextBox();
        XSLFTextRun text = emailLabel.setText("Email");
        text.setFontSize(10.0);
        text.setFontFamily(TEXT_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        Util.setSizeAndPosition(emailLabel, 4.64, 7.2, 2.02, 0.6);
    }

    private void addEmailInput(XSLFSlide slide) {
        XSLFTextShape emailInput = slide.createTextBox();
        XSLFTextRun text = emailInput.setText(getString(this.pointInformation.getEmail()));
        text.setFontSize(10.0);
        text.setFontFamily(TEXT_FONT_FACE);
        text.setFontColor(Color.decode(TITLE_FONT));
        Util.setSizeAndPosition(emailInput, 6.14, 7.2, 3.95, 0.6);
    }
    
    private String getString(String string) {
        return string == null ? "" : string;
    }
}
