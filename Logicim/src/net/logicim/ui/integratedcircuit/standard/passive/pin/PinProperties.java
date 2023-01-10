package net.logicim.ui.integratedcircuit.standard.passive.pin;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class PinProperties
    extends ComponentProperties
{
  protected int bitWidth;

  public PinProperties()
  {
  }

  public PinProperties(String name, int bitWidth)
  {
    super(name);
    this.bitWidth = bitWidth;
  }
}

