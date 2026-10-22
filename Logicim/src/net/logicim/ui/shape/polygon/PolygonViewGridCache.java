package net.logicim.ui.shape.polygon;

import net.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.shape.common.GridCache;

import java.util.ArrayList;
import java.util.List;

public class PolygonViewGridCache
    extends GridCache
{
  protected List<Tuple2> transformedBuffer;
  protected int[] xArray;
  protected int[] yArray;

  public PolygonViewGridCache(List<Tuple2> points)
  {
    super();

    this.transformedBuffer = new ArrayList<>(points.size());
    for (Tuple2 point : points)
    {
      this.transformedBuffer.add(point.clone());
    }

    xArray = new int[points.size()];
    yArray = new int[points.size()];
  }

  public void update(List<Tuple2> points, Rotation rotation, Tuple2 position)
  {
    update();

    for (int i = 0; i < points.size(); i++)
    {
      Tuple2 dest = transformedBuffer.get(i);
      rotation.rotate(dest, points.get(i));
      dest.add(position);
    }
  }

  public int[] getXArray()
  {
    return xArray;
  }

  public int[] getYArray()
  {
    return yArray;
  }

  public List<Tuple2> getTransformedBuffer()
  {
    return transformedBuffer;
  }
}

