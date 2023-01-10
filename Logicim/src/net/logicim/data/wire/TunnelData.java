package net.logicim.data.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;

public class TunnelData
    extends ReflectiveData
{
  public long[] traceIds;
  public Int2D position;
  protected boolean selected;

  public TunnelData()
  {
  }

  public TunnelData(long[] traceIds,
                    Int2D position,
                    boolean selected)
  {

    this.traceIds = traceIds;
    this.position = position;
    this.selected = selected;
  }
}

