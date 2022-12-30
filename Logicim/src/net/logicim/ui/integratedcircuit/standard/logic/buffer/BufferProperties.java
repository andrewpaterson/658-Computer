package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.integratedcircuit.standard.common.StandardIntegratedCircuitProperties;

public class BufferProperties
    extends StandardIntegratedCircuitProperties
{
  public BufferProperties()
  {
  }

  public BufferProperties(String name, Family family, boolean explicitPowerPorts)
  {
    super(name, family, explicitPowerPorts);
  }
}
