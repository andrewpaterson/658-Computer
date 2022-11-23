package net.logicim.data.trace;

import net.logicim.common.type.Int2D;

public class TraceData
{
  protected long id;

  protected Int2D start;
  protected Int2D end;

  public TraceData(long id, Int2D start, Int2D end)
  {
    this.id = id;
    this.start = start;
    this.end = end;
  }
}

