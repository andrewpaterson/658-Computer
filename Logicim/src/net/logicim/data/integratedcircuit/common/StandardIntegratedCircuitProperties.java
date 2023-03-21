package net.logicim.data.integratedcircuit.common;

import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitProperties;

public abstract class StandardIntegratedCircuitProperties
    extends IntegratedCircuitProperties
{
  public boolean explicitPowerPorts;

  public StandardIntegratedCircuitProperties()
  {
    explicitPowerPorts = false;
  }

  public StandardIntegratedCircuitProperties(String name, Family family, boolean explicitPowerPorts)
  {
    super(name, family);
    this.explicitPowerPorts = explicitPowerPorts;
  }
}

