package net.logicim.ui.shape.line;

import net.common.type.Tuple2;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.shape.common.GridCache;

public class LineGridCache
    extends GridCache
{
  protected Tuple2 transformedStart;
  protected Tuple2 transformedEnd;

  public LineGridCache(Tuple2 start, Tuple2 end)
  {
    super();
    transformedEnd = start.clone();
    transformedStart = end.clone();
  }

  public void update(Tuple2 start, Tuple2 end, Rotation rotation, Tuple2 position)
  {
    update();

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

