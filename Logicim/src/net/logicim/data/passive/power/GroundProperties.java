package net.logicim.data.passive.power;

import net.logicim.data.common.properties.ComponentProperties;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    GroundProperties that = (GroundProperties) o;
    return Objects.equals(name, that.name);
  }
}

