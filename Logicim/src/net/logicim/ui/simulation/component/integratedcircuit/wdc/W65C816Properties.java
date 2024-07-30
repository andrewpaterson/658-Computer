package net.logicim.ui.simulation.component.integratedcircuit.wdc;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.StandardIntegratedCircuitProperties;

public class W65C816Properties
    extends StandardIntegratedCircuitProperties
{
  public W65C816Properties()
  {
    super();
  }

  public W65C816Properties(String name,
                           Family family,
                           boolean explicitPowerPorts)
  {
    super(name,
          family,
          explicitPowerPorts);
  }

  @Override
  public ComponentProperties duplicate()
  {
    return new W65C816Properties(name,
                                 family,
                                 explicitPowerPorts);
  }
}

