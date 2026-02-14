package net.logicim.ui.simulation.component.integratedcircuit.standard.probe;

import net.logicim.data.common.Radix;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;

public class ProbeProperties
    extends StandardIntegratedCircuitProperties
{
  public int bitWidth;
  public Radix radix;

  public ProbeProperties()
  {
  }

  public ProbeProperties(String name,
                         Family family,
                         int bitWidth,
                         Radix radix)
  {
    super(name, family, false);
    this.bitWidth = bitWidth;
    this.radix = radix;
  }

  @Override
  public ProbeProperties duplicate()
  {
    return new ProbeProperties(name,
                               family,
                               bitWidth,
                               radix);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof ProbeProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }

    ProbeProperties that = (ProbeProperties) o;
    return bitWidth == that.bitWidth &&
           radix == that.radix;
  }
}

