package net.logicim.ui.common;

import java.awt.*;
import java.awt.geom.Point2D;

public class Viewport
{
  protected Point2D.Float position;
  protected float zoom;
  protected float scale;
  protected PanelSize size;
  protected Color smallGridDotColor;
  protected Color largeGridDotColor;

  public Viewport(PanelSize size)
  {
    this.size = size;
    position = new Point2D.Float(0, 0);
    zoom = 1.0f;
    scale = 10.0f;
    smallGridDotColor = new Color(0xCBCBCB);
    largeGridDotColor = new Color(0xABABAB);
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
    int left = -(int) ((centerX + position.x) / (scale * zoom)) - 1;
    int dotsAcross = (int) (size.getWidth() / (scale * zoom)) + 2;
    int top = -(int) ((centerY + position.y) / (scale * zoom)) - 1;
    int dotsDown = (int) (size.getHeight() / (scale * zoom)) + 2;

    if ((zoom >= 0.3f) && (zoom <= 2.0f))
    {
      graphics.setColor(smallGridDotColor);
      for (int y = 0; y < dotsDown; y++)
      {
        int transformY = transformY(top + y) + (int) position.y;
        for (int x = 0; x < dotsAcross; x++)
        {
          int transformX = transformX(left + x) + (int) position.x;
          graphics.drawLine(transformX, transformY, transformX, transformY);
        }
      }
    }
    else if (zoom > 2.0f)
    {
      graphics.setColor(largeGridDotColor);
      for (int y = 0; y < dotsDown; y++)
      {
        int transformY = transformY(top + y) + (int) position.y;
        for (int x = 0; x < dotsAcross; x++)
        {
          int transformX = transformX(left + x) + (int) position.x;
          graphics.fillOval(transformX, transformY, (int) zoom, (int) zoom);
        }
      }
    }

    graphics.setColor(color);
  }

  public void scroll(Position relative)
  {
    position.x -= relative.x;
    position.y -= relative.y;
  }

  public void zoom(float zoom)
  {
    this.zoom -= zoom;
    if (this.zoom < 0.2f)
    {
      this.zoom = 0.2f;
    }
    if (this.zoom > 6.0f)
    {
      this.zoom = 6.0f;
    }
  }
}

