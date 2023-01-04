package net.logicim.ui.shape.common;

import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class ShapeView
{
  protected ShapeHolder shapeHolder;
  protected Color fillColour;
  protected Color borderColour;

  public ShapeView(ShapeHolder shapeHolder)
  {
    this.shapeHolder = shapeHolder;
    shapeHolder.add(this);
    this.fillColour = null;
    this.borderColour = null;
  }

  public ShapeView(ShapeHolder shapeHolder, Color fillColour, Color borderColour)
  {
    this.shapeHolder = shapeHolder;
    shapeHolder.add(this);
    this.fillColour = fillColour;
    this.borderColour = borderColour;
  }

  public abstract void paint(Graphics2D graphics, Viewport viewport);

  protected Color getFillColour()
  {
    if (fillColour == null)
    {
      return Colours.getInstance().getShapeFill();
    }
    else
    {
      return fillColour;
    }
  }

  protected Color getBorderColour()
  {
    if (borderColour == null)
    {
      return Colours.getInstance().getShapeBorder();
    }
    else
    {
      return borderColour;
    }
  }

  public void setFillColour(Color fillColour)
  {
    this.fillColour = fillColour;
  }

  public void setBorderColour(Color borderColour)
  {
    this.borderColour = borderColour;
  }

  public abstract void boundingBoxInclude(BoundingBox boundingBox);

  public abstract void invalidateCache();
}

