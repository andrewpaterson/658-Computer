package net.logicim.data.port;

import net.logicim.data.ReflectiveData;

public class PortData
    extends ReflectiveData
{
  public long traceId;

  public PortData()
  {
  }

  public PortData(long traceId)
  {
    this.traceId = traceId;
  }
}

