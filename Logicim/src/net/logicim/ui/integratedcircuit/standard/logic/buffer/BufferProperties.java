package net.logicim.ui.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.integratedcircuit.standard.common.StandardIntegratedCircuitProperties;

public class BufferProperties
    extends StandardIntegratedCircuitProperties
{
  public int bufferCount;

  public BufferProperties()
  {
    bufferCount = 0;
  }

  public BufferProperties(String name, Family family, boolean explicitPowerPorts, int bufferCount)
  {
    super(name, family, explicitPowerPorts);
    this.bufferCount = bufferCount;
  }
}

