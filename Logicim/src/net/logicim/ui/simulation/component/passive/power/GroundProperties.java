package net.logicim.ui.simulation.component.passive.power;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class GroundProperties
    extends ComponentProperties
{
  public GroundProperties()
  {
  }

  public GroundProperties(String name)
  {
    super(name);
  }

  @Override
  public GroundProperties duplicate()
  {
    return new GroundProperties(name);
  }
}

