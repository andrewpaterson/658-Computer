package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class PinProperties
    extends ComponentProperties
{
  protected int bitWidth;
  protected SubcircuitPinAlignment alignment;
  protected boolean inverting;
  protected boolean overline;
  protected boolean clockNotch;

  public PinProperties()
  {
  }

  public PinProperties(String name,
                       int bitWidth,
                       SubcircuitPinAlignment alignment,
                       boolean inverting,
                       boolean overline,
                       boolean clockNotch)
  {
    super(name);
    this.bitWidth = bitWidth;
    this.alignment = alignment;
    this.inverting = inverting;
    this.overline = overline;
    this.clockNotch = clockNotch;
  }

  @Override
  public PinProperties duplicate()
  {
    return new PinProperties(name,
                             bitWidth,
                             alignment,
                             inverting,
                             overline,
                             clockNotch);
  }
}

