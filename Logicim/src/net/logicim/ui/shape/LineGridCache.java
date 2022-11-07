package net.logicim.ui.shape;

import net.logicim.common.type.Tuple2;
import net.logicim.ui.common.Rotation;

public class LineGridCache
{
  protected Tuple2 transformedStart;
  protected Tuple2 transformedEnd;
  protected boolean valid;

  public LineGridCache(Tuple2 start, Tuple2 end)
  {
    transformedEnd = start.clone();
    transformedStart = end.clone();
    valid = false;
  }

  public boolean isValid()
  {
    return valid;
  }

  public void invalidate()
  {
    valid = false;
  }

  public void update(Tuple2 start, Tuple2 end, Rotation rotation, Tuple2 position)
  {
    valid = true;
    rotation.rotate(transformedEnd, end);
    rotation.rotate(transformedStart, start);

    transformedStart.add(position);
    transformedEnd.add(position);
  }

  public Tuple2 getTransformedStart()
  {
    return transformedStart;
  }

  public Tuple2 getTransformedEnd()
  {
    return transformedEnd;
  }
}

