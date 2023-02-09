package net.logicim.ui.simulation.component.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitProperties;

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
}

