package net.logicim.ui.util;

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

  public static Point ensureOnScreen(Point location, Dimension dimension)
  {
    Dimension screenSize = getScreenSize();
    int border = 20;
    int right = dimension.width + location.x + border;
    if (right > screenSize.width)
    {
      location.x -= (right - screenSize.width);
    }
    int bottom = dimension.height + location.y + border;
    if (bottom > screenSize.height)
    {
      location.y -= (bottom - screenSize.height);
    }

    if (location.x < border)
    {
      location.x = border;
    }
    if (location.y < border)
    {
      location.y = border;
    }

    return location;
  }
}

