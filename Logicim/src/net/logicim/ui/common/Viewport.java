package net.logicim.ui.common;

import java.awt.*;
import java.awt.geom.Point2D;

public class Viewport
{
  protected Point2D.Float position;
  protected float zoom;
  protected float scale;
  protected PanelSize size;

  public Viewport(PanelSize size)
  {
    this.size = size;
    position = new Point2D.Float(0, 0);
    zoom = 1.0f;
    scale = 50.0f;
  }

  public int transformX(int x)
  {
    int center = size.getWidth() / 2;
    return (int) (x * scale * zoom + center);
  }

  public int transformY(int y)
  {
    int center = size.getHeight() / 2;
    return (int) (y * scale * zoom + center);
  }

  public int transformWidth(int x)
  {
    return (int) (x * scale * zoom);
  }

  public int transformHeight(int y)
  {
    return (int) (y * scale * zoom);
  }

  public void paintGrid(Graphics2D graphics)
  {
    Color color = graphics.getColor();

    int centerX = size.getWidth() / 2;
    int centerY = size.getHeight() / 2;
    int left = -(int) ((centerX) / (scale * zoom));
    int dotsAcross = (int) (size.getWidth() / (scale * zoom));
    int top = -(int) ((centerY) / (scale * zoom));
    int dotsDown = (int) (size.getHeight() / (scale * zoom));

    graphics.setColor(Color.DARK_GRAY);
    for (int y = top; y < dotsDown; y++)
    {
      int transformY = transformY(y);
      for (int x = left; x < dotsAcross; x++)
      {
        int transformX = transformX(x);
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawString("" + x + ", " + y, transformX, transformY);
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillOval(transformX, transformY, 3, 3);
      }
    }

    graphics.setColor(color);
  }
}

