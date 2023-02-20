package net.logicim.ui.common.integratedcircuit;

import net.logicim.domain.common.propagation.Family;

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
