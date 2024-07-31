package net.logicim.ui.shape.common;

import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

public abstract class ShapeView
{
  protected ShapeHolder shapeHolder;
  protected Color fillColour;
  protected Color borderColour;
  protected int relativeRightRotations;

  public ShapeView(ShapeHolder shapeHolder)
  {
    this.shapeHolder = shapeHolder;
    shapeHolder.add(this);
    this.fillColour = null;
    this.borderColour = null;
    this.relativeRightRotations = 0;
  }

  public ShapeView(ShapeHolder shapeHolder, Color fillColour, Color borderColour)
  {
    this.shapeHolder = shapeHolder;
    shapeHolder.add(this);
    this.fillColour = fillColour;
    this.borderColour = borderColour;
    this.relativeRightRotations = 0;
  }

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

  protected Rotation getShapeHolderRotation()
  {
    if (relativeRightRotations == 0)
    {
      return shapeHolder.getRotation();
    }
    else
    {
      return shapeHolder.getRotation().rotateRight(relativeRightRotations);
    }
  }

  protected void transformAndIncludeLocalBox(BoundingBox boundingBox, BoundingBox localBox)
  {
    localBox.transform(Rotation.North.rotateRight(relativeRightRotations));
    boundingBox.include(localBox.getTransformedTopLeft());
    boundingBox.include(localBox.getTransformedBottomRight());
  }

  public void setRelativeRightRotations(int relativeRightRotations)
  {
    this.relativeRightRotations = relativeRightRotations;
  }

  public abstract void paint(Graphics2D graphics, Viewport viewport);

  public RectangleView setFillColour(Color fillColour)
  {
    this.fillColour = fillColour;
    return null;
  }

  public void setBorderColour(Color borderColour)
  {
    this.borderColour = borderColour;
  }

  public abstract void boundingBoxInclude(BoundingBox boundingBox);

  public abstract void invalidateCache();
}

