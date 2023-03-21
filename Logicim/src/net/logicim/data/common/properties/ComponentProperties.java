package net.logicim.data.common.properties;

import net.logicim.common.SimulatorException;
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
    throw new SimulatorException(getClass().getSimpleName() + ".equals not implemented.");
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(name);
  }

  public abstract ComponentProperties duplicate();
}

