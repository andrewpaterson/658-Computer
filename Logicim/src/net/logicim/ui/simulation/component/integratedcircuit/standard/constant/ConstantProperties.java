package net.logicim.ui.simulation.component.integratedcircuit.standard.constant;

import net.logicim.data.common.Radix;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;

public class ConstantProperties
    extends StandardIntegratedCircuitProperties
{
  public int bitWidth;
  public Radix radix;

  public ConstantProperties()
  {
  }

  public ConstantProperties(String name,
                            Family family,
                            int bitWidth,
                            Radix radix)
  {
    super(name, family, false);
    this.bitWidth = bitWidth;
    this.radix = radix;
  }

  @Override
  public ConstantProperties duplicate()
  {
    return new ConstantProperties(name,
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
    if (!(o instanceof ConstantProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }

    ConstantProperties that = (ConstantProperties) o;
    return bitWidth == that.bitWidth &&
           radix == that.radix;
  }
}

