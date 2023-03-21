package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;

public abstract class ComponentProperties
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

  public abstract ComponentProperties duplicate();
}

