package net.logicim.ui.common.subcircuit;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class SubcircuitInstanceProperties
    extends ComponentProperties
{
  public SubcircuitInstanceProperties(String name)
  {
    super(name);
  }

  @Override
  public ComponentProperties duplicate()
  {
    return new SubcircuitInstanceProperties(name);
  }
}

