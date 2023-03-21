package net.logicim.data.port.common;

import net.logicim.data.common.ReflectiveData;

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

