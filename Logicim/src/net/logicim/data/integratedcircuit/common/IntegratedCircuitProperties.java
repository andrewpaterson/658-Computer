package net.logicim.data.integratedcircuit.common;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.family.Family;

import java.util.Objects;

public abstract class IntegratedCircuitProperties
    extends ComponentProperties
{
  public Family family;

  public IntegratedCircuitProperties()
  {
    family = null;
  }

  public IntegratedCircuitProperties(String name, Family family)
  {
    super(name);
    this.family = family;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (!(o instanceof IntegratedCircuitProperties))
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    IntegratedCircuitProperties that = (IntegratedCircuitProperties) o;
    return Objects.equals(family, that.family);
  }
}

