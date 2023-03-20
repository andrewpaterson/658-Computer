package net.logicim.ui.simulation.component.integratedcircuit.standard.clock;

import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitProperties;

public class ClockProperties
    extends StandardIntegratedCircuitProperties
{
  public float frequency_Hz;
  public boolean inverseOut;

  public ClockProperties()
  {
    frequency_Hz = 0;
    inverseOut = false;
  }

  public ClockProperties(String name,
                         Family family,
                         boolean explicitPowerPorts,
                         float frequency,
                         boolean inverseOut)
  {
    super(name, family, explicitPowerPorts);
    this.frequency_Hz = frequency;
    this.inverseOut = inverseOut;
  }

  @Override
  public ClockProperties duplicate()
  {
    return new ClockProperties(name,
                               family,
                               explicitPowerPorts,
                               frequency_Hz,
                               inverseOut);
  }
}

