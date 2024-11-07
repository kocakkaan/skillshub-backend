package com.reply.skillshub.controllers.resume.powerpoint;

public class SharedValues {

    public SharedValues() {};

    public final static String TEXT_FONT_FACE = "PT Sans";
    public final static String TITLE_FONT_FACE = "PT Sans Caption";
    public final static String TITLE_FONT = "#FFFFFF";
    public final static double HEIGHT_TITLE_SHAPE = 0.68;
    public final static double STARTING_VERTICAL_POINT_FIRST_ROW_SHAPES = 3.6;
    public final static double START_SECOND_COLUMN = 22.5;
    public final static double START_FIRST_COLUMN = 1.02;
    public final static double START_SECOND_ROW = 8.52;
    public final static double WIDTH_FIRST_COLUMN = 9.36;
    public final static double WIDTH_SECOND_COLUMN = 22;
    public final static double WIDTH_EXPERTISE = 10.5;
    public final static double HEIGHT_EXPERTISE_TEXT = 3.3;
    public final static double HEIGHT_EXPERTISE_BACKGROUND = 4.55 - 0.68;
    

    public static String getTitleBackgroundColor(String company) {
      if (company == null) {
        return "#8D8EA8";
      }
      switch (company) {
        case "Reply":
          return "#00B13E";
        case "ML_Reply":
          return "#8D8EA8";
        default:
          return "#8D8EA8";
      }
    }

    public static String getTextCompanyColor(String company) {
      if (company == null) {
        return "#F8F8FA";
      }
      switch (company) {
        case "Reply":
          return "#f6fff5";
        case "ML_Reply":
          return "#F8F8FA";
        default:
          return "#F8F8FA";
      }
    }
}
