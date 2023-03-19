package net.logicim.ui.common.subcircuit;

import net.logicim.ui.common.integratedcircuit.ComponentProperties;

public class SubcircuitInstanceProperties
    extends ComponentProperties
{
  public String subcircuitTypeName;

  public SubcircuitInstanceProperties(String name,
                                      String subcircuitTypeName)
  {
    super(name);
    this.subcircuitTypeName = subcircuitTypeName;
  }

  @Override
  public ComponentProperties duplicate()
  {
    return new SubcircuitInstanceProperties(name,
                                            subcircuitTypeName);
  }
}

