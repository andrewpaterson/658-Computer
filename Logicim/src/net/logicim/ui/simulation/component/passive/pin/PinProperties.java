package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.common.Radix;
import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class PinProperties
    extends ComponentProperties
{
  protected int bitWidth;
  protected SubcircuitPinAlignment alignment;
  protected boolean inverting;
  protected boolean overline;
  protected boolean clockNotch;
  protected Family family;
  protected Radix radix;

  public PinProperties()
  {
  }

  public PinProperties(String name,
                       int bitWidth,
                       SubcircuitPinAlignment alignment,
                       boolean inverting,
                       boolean overline,
                       boolean clockNotch,
                       Family family,
                       Radix radix)
  {
    super(name);
    this.bitWidth = bitWidth;
    this.alignment = alignment;
    this.inverting = inverting;
    this.overline = overline;
    this.clockNotch = clockNotch;
    this.family = family;
    this.radix = radix;
  }

  @Override
  public PinProperties duplicate()
  {
    return new PinProperties(name,
                             bitWidth,
                             alignment,
                             inverting,
                             overline,
                             clockNotch,
                             family,
                             radix);
  }
}

