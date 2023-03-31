package net.logicim.data.integratedcircuit.standard.logic.common;

import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;

import java.util.Objects;

public class LogicGateProperties
    extends StandardIntegratedCircuitProperties
{
  public int inputCount;
  public int inputWidth;

  public LogicGateProperties()
  {
    inputCount = 0;
    inputWidth = 0;
  }

  public LogicGateProperties(String name,
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
  public LogicGateProperties duplicate()
  {
    return new LogicGateProperties(name,
                                   family,
                                   explicitPowerPorts,
                                   inputCount,
                                   inputWidth);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof LogicGateProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    LogicGateProperties that = (LogicGateProperties) o;
    return inputCount == that.inputCount &&
           inputWidth == that.inputWidth;
  }
}

