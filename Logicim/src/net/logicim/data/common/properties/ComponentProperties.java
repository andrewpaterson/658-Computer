package net.logicim.data.common.properties;

import net.logicim.data.common.ReflectiveData;

import java.util.Objects;

public abstract class ComponentProperties
    extends ReflectiveData
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
    if (!(o instanceof ComponentProperties))
    {
      return false;
    }
    ComponentProperties that = (ComponentProperties) o;
    return Objects.equals(name, that.name);
  }

  public abstract ComponentProperties duplicate();
}

