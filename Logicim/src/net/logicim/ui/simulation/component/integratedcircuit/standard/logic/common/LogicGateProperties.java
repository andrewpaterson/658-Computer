package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.common;

import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitProperties;

public class LogicGateProperties
    extends StandardIntegratedCircuitProperties
{
  public int inputCount;

  public LogicGateProperties()
  {
    inputCount = 0;
  }

  public LogicGateProperties(String name, Family family, boolean explicitPowerPorts, int inputCount)
  {
    super(name, family, explicitPowerPorts);
    this.inputCount = inputCount;
  }
}

