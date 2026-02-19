package net.logicim.ui.simulation.component.integratedcircuit.standard.flop.dtype;

import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;

public class DTypeFlipFlopProperties
    extends StandardIntegratedCircuitProperties
{
  public boolean inverseOut;
  public boolean setReset;

  public DTypeFlipFlopProperties()
  {
  }

  public DTypeFlipFlopProperties(String name,
                                 Family family,
                                 boolean explicitPowerPorts,
                                 boolean inverseOut,
                                 boolean setReset)
  {
    super(name, family, explicitPowerPorts);
    this.inverseOut = inverseOut;
    this.setReset = setReset;
  }

  @Override
  public DTypeFlipFlopProperties duplicate()
  {
    return new DTypeFlipFlopProperties(name,
                                       family,
                                       explicitPowerPorts,
                                       inverseOut,
                                       setReset);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof DTypeFlipFlopProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    DTypeFlipFlopProperties that = (DTypeFlipFlopProperties) o;
    return setReset == that.setReset &&
           inverseOut == that.inverseOut;
  }
}

