package net.logicim.ui.common.integratedcircuit;

import net.logicim.domain.common.propagation.Family;

public class IntegratedCircuitProperties
    extends DiscreteProperties
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
