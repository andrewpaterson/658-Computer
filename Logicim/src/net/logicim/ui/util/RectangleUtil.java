package net.logicim.ui.util;

import java.awt.*;

public class RectangleUtil
{
  public static void drawRect(Graphics2D graphics, int x1, int y1, int x2, int y2)
  {
    if (x1 > x2)
    {
      int i = x2;
      x2 = x1;
      x1 = i;
    }
    if (y1 > y2)
    {
      int i = y2;
      y2 = y1;
      y1 = i;
    }
    graphics.drawRect(x1, y1, x2 - x1, y2 - y1);
  }
}

