package net.logicim.ui.simulation.component.passive.pin;

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

  @Override
  public PinProperties duplicate()
  {
    return new PinProperties(name, bitWidth);
  }
}

