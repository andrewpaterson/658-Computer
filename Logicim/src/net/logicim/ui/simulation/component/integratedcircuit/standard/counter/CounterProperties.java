package net.logicim.ui.simulation.component.integratedcircuit.standard.counter;

import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;

public class CounterProperties
    extends StandardIntegratedCircuitProperties
{
  public int bitWidth;
  public int terminalValue;

  public CounterProperties()
  {
  }

  public CounterProperties(String name,
                           Family family,
                           boolean explicitPowerPorts,
                           int bitWidth,
                           int terminalValue)
  {
    super(name, family, explicitPowerPorts);
    this.bitWidth = bitWidth;
    this.terminalValue = terminalValue;
  }

  @Override
  public CounterProperties duplicate()
  {
    return new CounterProperties(name,
                                 family,
                                 explicitPowerPorts,
                                 bitWidth,
                                 terminalValue);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof CounterProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    CounterProperties that = (CounterProperties) o;
    return bitWidth == that.bitWidth &&
           terminalValue == that.terminalValue;
  }
}

