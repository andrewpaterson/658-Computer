package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.components.typeeditor.TextPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

public class SubcircuitPropertiesPanel
    extends PropertiesPanel
{
  public static final String TYPE_NAME = "Name";

  protected TextPropertyEditor name;

  public SubcircuitPropertiesPanel(String name)
  {
    super();
    this.name = new TextPropertyEditor(TYPE_NAME, name);
    addLabeledComponent(TYPE_NAME, this.name.getComponent());
  }

  public String getSubcircuitName()
  {
    return name.getValue();
  }

  @Override
  public ComponentProperties createProperties(ComponentProperties oldProperties)
  {
    throw new SimulatorException("Cannot call create properties for SubcircuitPropertiesPanel.");
  }
}

