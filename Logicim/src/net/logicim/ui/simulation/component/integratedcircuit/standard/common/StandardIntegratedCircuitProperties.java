package net.logicim.ui.simulation.component.integratedcircuit.standard.common;

import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitProperties;

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

