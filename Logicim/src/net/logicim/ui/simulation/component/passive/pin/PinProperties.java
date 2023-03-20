package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.data.circuit.SubcircuitPinPosition;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.data.common.Radix;
import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class PinProperties
    extends ComponentProperties
{
  protected int bitWidth;
  protected SubcircuitPinPosition alignment;
  protected SubcircuitPinAnchour offset;
  protected int weight;
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
                       SubcircuitPinPosition alignment,
                       SubcircuitPinAnchour offset,
                       int weight,
                       boolean inverting,
                       boolean overline,
                       boolean clockNotch,
                       Family family,
                       Radix radix)
  {
    super(name);
    this.bitWidth = bitWidth;
    this.alignment = alignment;
    this.offset = offset;
    this.weight = weight;
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
                             offset,
                             weight,
                             inverting,
                             overline,
                             clockNotch,
                             family,
                             radix);
  }
}

