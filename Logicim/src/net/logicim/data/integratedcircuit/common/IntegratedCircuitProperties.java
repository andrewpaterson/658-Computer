package net.logicim.data.integratedcircuit.common;

import net.logicim.data.family.Family;
import net.logicim.data.common.properties.ComponentProperties;

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
}

