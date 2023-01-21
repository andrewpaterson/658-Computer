package net.logicim.ui.common.integratedcircuit;

import java.util.Objects;

public class ComponentProperties
{
  public String name;

  public ComponentProperties()
  {
    name = null;
  }

  public ComponentProperties(String name)
  {
    this.name = name;
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
    ComponentProperties that = (ComponentProperties) o;
    return Objects.equals(name, that.name);
  }
}
