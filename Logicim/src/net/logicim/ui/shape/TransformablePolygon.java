package net.logicim.ui.shape;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;

import java.util.ArrayList;
import java.util.List;

public class TransformablePolygon
{
  protected List<Tuple2> points;
  protected List<Tuple2> transformBuffer;

  public TransformablePolygon(Tuple2... points)
  {
    this.points = new ArrayList<>(points.length);
    this.transformBuffer = new ArrayList<>(points.length);
    for (Tuple2 point : points)
    {
      this.points.add(point);
      this.transformBuffer.add(point.clone());
    }
  }

  public void rotate(Rotation r)
  {
    for (int i = 0; i < points.size(); i++)
    {
      Tuple2 source = points.get(i);
      Tuple2 dest = transformBuffer.get(i);
      dest.set(source);
      if (dest instanceof Int2D)
      {
        r.transform((Int2D) dest);
      }
      else if (dest instanceof Float2D)
      {
        r.transform((Float2D) dest);
      }
    }
  }

  public List<Tuple2> getTransformBuffer()
  {
    return transformBuffer;
  }
}

