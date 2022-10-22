package net.logicim.ui;

import java.awt.*;

public class WindowSizer
{
  public static void centre(Window window)
  {
    Component parent = window.getParent();
    window.setLocationRelativeTo(parent);
  }

  private static Dimension getScreenSize()
  {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

  public static double getPercentageOfScreenHeight(double percent)
  {
    Dimension screenDimension = getScreenSize();
    return ((screenDimension.getHeight() * percent) / 100);
  }

  public static double getPercentageOfScreenWidth(double percent)
  {
    Dimension screenDimension = getScreenSize();
    return ((screenDimension.getWidth() * percent) / 100.0);
  }

  public static void setPercentageOfScreenSize(Container container, double x, double y)
  {
    double width = getPercentageOfScreenWidth(x);
    double height = getPercentageOfScreenHeight(y);
    container.setSize((int) width, (int) height);
  }
}

