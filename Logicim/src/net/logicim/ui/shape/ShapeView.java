package net.logicim.ui.shape;

import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class ShapeView
{
  protected ShapeHolder shapeHolder;

  public ShapeView(ShapeHolder shapeHolder)
  {
    this.shapeHolder = shapeHolder;
    shapeHolder.add(this);
  }

  public abstract void paint(Graphics2D graphics, Viewport viewport);

  public abstract void boundingBoxInclude(BoundingBox boundingBox);

  public abstract void invalidateCache();
}

