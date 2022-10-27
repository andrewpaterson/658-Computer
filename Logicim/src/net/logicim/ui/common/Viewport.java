package net.logicim.ui.common;

import java.awt.geom.Point2D;

public class Viewport
{
  protected Point2D.Float position;
  protected float zoom;
  protected float scale;
  protected PanelSize size;

  public Viewport()
  {
    position = new Point2D.Float(0, 0);
    zoom = 1.0f;
    scale = 11.0f;
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
}

