package net.logicim.data.integratedcircuit.common;

import net.logicim.data.family.Family;

import java.util.Objects;

public abstract class StandardIntegratedCircuitProperties
    extends IntegratedCircuitProperties
{
  public boolean explicitPowerPorts;

  public StandardIntegratedCircuitProperties()
  {
    explicitPowerPorts = false;
  }

  public StandardIntegratedCircuitProperties(String name,
                                             Family family,
                                             boolean explicitPowerPorts)
  {
    super(name, family);
    this.explicitPowerPorts = explicitPowerPorts;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof StandardIntegratedCircuitProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    StandardIntegratedCircuitProperties that = (StandardIntegratedCircuitProperties) o;
    return explicitPowerPorts == that.explicitPowerPorts;
  }
}

