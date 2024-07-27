package net.logicim.data.port.common;

import net.logicim.data.common.ReflectiveData;

import java.util.List;

public class MultiPortData
    extends ReflectiveData
{
  public List<PortData> ports;

  public MultiPortData()
  {
  }

  public MultiPortData(List<PortData> ports)
  {
    this.ports = ports;
  }
}

