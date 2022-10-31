package net.logicim.ui.shape;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

public class PolygonViewGridCache
{
  protected List<Tuple2> transformedBuffer;
  protected int[] xArray;
  protected int[] yArray;
  protected boolean valid;

  public PolygonViewGridCache(List<Tuple2> points)
  {
    this.transformedBuffer = new ArrayList<>(points.size());
    for (Tuple2 point : points)
    {
      this.transformedBuffer.add(point.clone());
    }

    xArray = new int[points.size()];
    yArray = new int[points.size()];
    valid = false;
  }

  public void invalidate()
  {
    valid = false;
  }

  public boolean isValid()
  {
    return valid;
  }

  public void update(List<Tuple2> points, Rotation rotation, Tuple2 position)
  {
    valid = true;
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

