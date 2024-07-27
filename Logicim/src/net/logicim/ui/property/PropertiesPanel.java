package net.logicim.ui.property;

import net.logicim.data.common.properties.ComponentProperties;

public abstract class PropertiesPanel
    extends FormPanel
{
  public PropertiesPanel()
  {
    super();
  }

  public abstract ComponentProperties createProperties(ComponentProperties oldProperties);
}

