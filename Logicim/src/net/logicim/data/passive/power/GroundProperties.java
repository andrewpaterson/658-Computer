package net.logicim.data.passive.power;

import net.logicim.data.common.properties.ComponentProperties;

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

