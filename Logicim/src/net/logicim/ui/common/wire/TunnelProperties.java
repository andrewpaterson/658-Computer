package net.logicim.ui.common.wire;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class TunnelProperties
    extends ComponentProperties
{
  protected boolean doubleSided;

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
}

