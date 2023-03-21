package net.logicim.data.passive.wire;

import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.data.common.Radix;
import net.logicim.data.family.Family;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.simulation.component.ui.Divider;

import java.util.Objects;

public class PinProperties
    extends ComponentProperties
{
  public int bitWidth;
  public Family family;
  public boolean explicitPowerPorts;
  public Divider instanceLocation;
  public SubcircuitPinAlignment alignment;
  public SubcircuitPinAnchour anchour;
  public int weight;
  public Divider instanceDisplay;
  public boolean inverting;
  public boolean clockNotch;
  public Radix radix;

  public PinProperties()
  {
  }

  public PinProperties(String name,
                       int bitWidth,
                       SubcircuitPinAlignment alignment,
                       SubcircuitPinAnchour anchour,
                       int weight,
                       boolean inverting,
                       boolean clockNotch,
                       Family family,
                       boolean explicitPowerPorts,
                       Radix radix)
  {
    super(name);
    this.explicitPowerPorts = explicitPowerPorts;
    this.instanceLocation = null;
    this.instanceDisplay = null;
    this.bitWidth = bitWidth;
    this.alignment = alignment;
    this.anchour = anchour;
    this.weight = weight;
    this.inverting = inverting;
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
                             anchour,
                             weight,
                             inverting,
                             clockNotch,
                             family,
                             explicitPowerPorts,
                             radix);
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
    PinProperties that = (PinProperties) o;
    return bitWidth == that.bitWidth &&
           explicitPowerPorts == that.explicitPowerPorts &&
           weight == that.weight &&
           inverting == that.inverting &&
           clockNotch == that.clockNotch &&
           Objects.equals(family, that.family) &&
           instanceLocation == that.instanceLocation &&
           alignment == that.alignment &&
           anchour == that.anchour &&
           instanceDisplay == that.instanceDisplay &&
           radix == that.radix &&
           Objects.equals(name, that.name);
  }
}

