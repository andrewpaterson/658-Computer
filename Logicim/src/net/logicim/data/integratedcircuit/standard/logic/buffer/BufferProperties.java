package net.logicim.data.integratedcircuit.standard.logic.buffer;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;

public class BufferProperties
    extends StandardIntegratedCircuitProperties
{
  public int inputCount;
  public int inputWidth;

  public BufferProperties()
  {
    inputCount = 0;
    inputWidth = 0;
  }

  public BufferProperties(String name,
                          Family family,
                          boolean explicitPowerPorts,
                          int inputCount,
                          int inputWidth)
  {
    super(name, family, explicitPowerPorts);
    this.inputCount = inputCount;
    this.inputWidth = inputWidth;
  }

  @Override
  public ComponentProperties duplicate()
  {
    return new BufferProperties(name, family, explicitPowerPorts, inputCount, inputWidth);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof BufferProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    BufferProperties that = (BufferProperties) o;
    return inputCount == that.inputCount &&
           inputWidth == that.inputWidth;
  }
}

