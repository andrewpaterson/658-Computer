package net.logicim.ui.common;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;

import java.awt.*;

public class Viewport
{
  protected Float2D position;
  protected float zoom;
  protected float scale;
  protected PanelSize size;
  protected Colours colours;
  protected Strokes strokes;

  public Viewport(PanelSize size)
  {
    this.size = size;
    position = new Float2D(0, 0);
    zoom = 1.0f;
    scale = 10.0f;
    colours = new Colours();
    strokes = new Strokes();
  }

  public int transformGridToScreenSpaceX(int x)
  {
    return (int) (x * scale * zoom + size.getWidth() / 2 + position.x);
  }

  public int transformGridToScreenSpaceY(int y)
  {
    return (int) (y * scale * zoom + size.getHeight() / 2 + position.y);
  }

  public int transformGridToScreenSpaceX(float x)
  {
    return (int) (x * scale * zoom + size.getWidth() / 2 + position.x);
  }

  public int transformGridToScreenSpaceY(float y)
  {
    return (int) (y * scale * zoom + size.getHeight() / 2 + position.y);
  }

  public int transformGridToScreenWidth(int x)
  {
    return (int) (x * scale * zoom);
  }

  public int transformGridToScreenHeight(int y)
  {
    return (int) (y * scale * zoom);
  }

  public int transformGridToScreenWidth(float x)
  {
    return (int) (x * scale * zoom);
  }

  public int transformGridToScreenHeight(float y)
  {
    return (int) (y * scale * zoom);
  }

  public int transformScreenToGridX(int x)
  {
    int center = x - size.getWidth() / 2;
    return Math.round((center - position.x) / (scale * zoom));
  }

  public int transformScreenToGridY(int y)
  {
    int center = y - size.getHeight() / 2;
    return Math.round((center - position.y) / (scale * zoom));
  }

  public float getLineWidth()
  {
    if (zoom < 0.5f)
    {
      return 1;
    }
    else
    {
      return zoom * 2.0f;
    }
  }

  public float getCircleRadius()
  {
    if (zoom < 0.3)
    {
      return 0.3f;
    }
    else
    {
      return zoom;
    }
  }

  public float getZoom()
  {
    return zoom;
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

    if ((zoom >= 0.3f) && (zoom < 1.5f))
    {
      drawDotGrid(graphics, left, dotsAcross, top, dotsDown, colours.getSmallGridDotColor());
    }
    if ((zoom >= 1.5f) && (zoom < 2.0f))
    {
      drawDotGrid(graphics, left, dotsAcross, top, dotsDown, colours.getLargeGridDotColor());
    }
    else if (zoom >= 2.0f)
    {
      drawCircleGrid(graphics, left, dotsAcross, top, dotsDown, colours.getLargeGridDotColor());
    }

    graphics.setColor(color);
  }

  private void drawCircleGrid(Graphics2D graphics, int left, int dotsAcross, int top, int dotsDown, Color color)
  {
    graphics.setColor(color);
    for (int y = 0; y < dotsDown; y++)
    {
      int transformY = transformGridToScreenSpaceY(top + y);
      for (int x = 0; x < dotsAcross; x++)
      {
        int transformX = transformGridToScreenSpaceX(left + x);
        int zoomHalf = (int) (zoom * 0.5);
        graphics.fillOval(transformX - zoomHalf, transformY - zoomHalf, (int) zoom, (int) zoom);
      }
    }
  }

  private void drawDotGrid(Graphics2D graphics, int left, int dotsAcross, int top, int dotsDown, Color color)
  {
    graphics.setStroke(getStroke(1));
    graphics.setColor(color);
    for (int y = 0; y < dotsDown; y++)
    {
      int transformY = transformGridToScreenSpaceY(top + y);
      for (int x = 0; x < dotsAcross; x++)
      {
        int transformX = transformGridToScreenSpaceX(left + x);
        graphics.drawLine(transformX, transformY, transformX, transformY);
      }
    }
  }

  public void scroll(Int2D relative)
  {
    position.x -= relative.x;
    position.y -= relative.y;
  }

  public void zoomTo(Int2D mousePosition, float zoom)
  {
    Float2D positionInGrid = transformScreenSpaceToGrid(mousePosition);
    zoom(zoom);
    Float2D positionInGridZoomed = transformScreenSpaceToGrid(mousePosition);
    positionInGrid.subtract(positionInGridZoomed);
    int width = transformGridToScreenWidth(positionInGrid.x);
    int height = transformGridToScreenHeight(positionInGrid.y);
    position.subtract(width, height);
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

  public Float2D transformGridToScreenSpace(Float2D p)
  {
    return new Float2D(p.x * scale * zoom + size.getWidth() / 2.0f + position.x,
                       p.y * scale * zoom + size.getHeight() / 2.0f + position.y);
  }

  public Float2D transformScreenSpaceToGrid(Int2D p)
  {
    float centerX = p.x - size.getWidth() / 2.0f;
    float fx = (centerX - position.x) / (scale * zoom);
    float centerY = p.y - size.getHeight() / 2.0f;
    float fy = (centerY - position.y) / (scale * zoom);
    return new Float2D(fx, fy);
  }

  public Colours getColours()
  {
    return colours;
  }

  public void paintRectangle(Graphics2D graphics,
                             int x,
                             int y,
                             int width,
                             int height,
                             Stroke stroke,
                             Color shapeFill,
                             Color shapeBorder)
  {
    if (width < 0)
    {
      x += width;
      width *= -1;
    }
    if (height < 0)
    {
      y += height;
      height *= -1;
    }

    graphics.setStroke(stroke);
    if (shapeFill != null)
    {
      graphics.setColor(shapeFill);
      graphics.fillRect(x, y, width, height);
    }
    if (shapeBorder != null)
    {
      graphics.setColor(shapeBorder);
      graphics.drawRect(x, y, width, height);
    }
  }

  public int transformGridToScreenWidth(Tuple2 tuple2)
  {
    if (tuple2 instanceof Int2D)
    {
      return transformGridToScreenWidth(((Int2D) tuple2).x);
    }
    else if (tuple2 instanceof Float2D)
    {
      return transformGridToScreenWidth(((Float2D) tuple2).x);
    }

    return 0;
  }

  public int transformGridToScreenHeight(Tuple2 tuple2)
  {
    if (tuple2 instanceof Int2D)
    {
      return transformGridToScreenHeight(((Int2D) tuple2).y);
    }
    else if (tuple2 instanceof Float2D)
    {
      return transformGridToScreenHeight(((Float2D) tuple2).y);
    }

    return 0;
  }

  public int transformGridToScreenSpaceX(Tuple2 tuple2)
  {
    if (tuple2 instanceof Int2D)
    {
      return transformGridToScreenSpaceX(((Int2D) tuple2).x);
    }
    else if (tuple2 instanceof Float2D)
    {
      return transformGridToScreenSpaceX(((Float2D) tuple2).x);
    }

    return 0;
  }

  public int transformGridToScreenSpaceY(Tuple2 tuple2)
  {
    if (tuple2 instanceof Int2D)
    {
      return transformGridToScreenSpaceY(((Int2D) tuple2).y);
    }
    else if (tuple2 instanceof Float2D)
    {
      return transformGridToScreenSpaceY(((Float2D) tuple2).y);
    }

    return 0;
  }

  public float getConnectionSize()
  {
    return 3;
  }

  public Stroke getStroke(float width)
  {
    return strokes.getStroke(width);
  }

  public Stroke getStroke()
  {
    return strokes.getStroke(getLineWidth());
  }
}

