package net.logicim.data.passive.wire;

import net.logicim.data.common.properties.ComponentProperties;

import java.util.Objects;

public class TunnelProperties
    extends ComponentProperties
{
  public boolean doubleSided;

  public TunnelProperties()
  {
  }

  public TunnelProperties(String name, boolean doubleSided)
  {
    super(name);
    this.doubleSided = doubleSided;
  }

  @Override
  public TunnelProperties duplicate()
  {
    return new TunnelProperties(name, doubleSided);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    TunnelProperties that = (TunnelProperties) o;
    return doubleSided == that.doubleSided &&
           Objects.equals(name, that.name);
  }
}

