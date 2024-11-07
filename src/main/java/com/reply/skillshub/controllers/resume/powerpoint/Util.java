package com.reply.skillshub.controllers.resume.powerpoint;

import java.awt.geom.Rectangle2D;

import org.apache.poi.util.Units;
import org.apache.poi.xslf.usermodel.XSLFSimpleShape;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

public class Util {

  private static int PIXELS_PER_INCH = 96;
  private static int PIXELS_PER_INCH_DOUBLE = 2 * PIXELS_PER_INCH;

  public static void setSizeAndPosition(XSLFSimpleShape shape, double x, double y, double width, double height) {
      shape.setAnchor(new Rectangle2D.Double(
          convertCmToPoint(x), 
          convertCmToPoint(y),
          convertCmToPoint(width),
          convertCmToPoint(height)));
  }

  private static double convertCmToPoint(double cm) {
    return cm * Units.EMU_PER_CENTIMETER / Units.EMU_PER_POINT;
  }


  private static double convertInchesToCm(double inches) {
      return inches * 2.54;
  }

  private static double convertCmToInches(double cm) {
      return cm / 2.54;
  }

  private static double convertPixelsToCm(int pixels) {
      return convertInchesToCm(convertPixelsToInches(pixels));
  }

  private static double convertPixelsToInches(int pixels) {
      return pixels / PIXELS_PER_INCH_DOUBLE;
  }

  public static int convertCmToPixels(double cm) {
      return convertInchesToPixels(convertCmToInches(cm));
  }

  private static int convertInchesToPixels(double inches) {
      return (int) Math.round(inches * PIXELS_PER_INCH_DOUBLE);
  }
    
}
