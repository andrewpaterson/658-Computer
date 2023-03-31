package net.logicim.data.passive.wire;

import net.logicim.data.common.properties.ComponentProperties;

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
    if (!(o instanceof TunnelProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    TunnelProperties that = (TunnelProperties) o;
    return doubleSided == that.doubleSided;
  }
}

