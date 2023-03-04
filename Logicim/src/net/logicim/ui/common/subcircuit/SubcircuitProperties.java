package net.logicim.ui.common.subcircuit;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class SubcircuitProperties
    extends ComponentProperties
{
  @Override
  public ComponentProperties duplicate()
  {
    return new SubcircuitProperties();
  }
}

