package net.logicim.ui.shape;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class ShapeView
{
  public ShapeView(ShapeHolder shapeHolder)
  {
    shapeHolder.add(this);
  }

  protected void rotate(Tuple2 dest, Tuple2 source, Rotation rotation)
  {
    dest.set(source);
    if (dest instanceof Int2D)
    {
      rotation.transform((Int2D) dest);
    }
    else if (dest instanceof Float2D)
    {
      rotation.transform((Float2D) dest);
    }
  }

  public abstract void paint(Graphics2D graphics, Viewport viewport, Rotation rotation, Int2D position);

  public abstract void boundingBoxInclude(BoundingBox boundingBox);
}

