package net.logicim.ui.simulation;

import net.logicim.ui.components.typeeditor.TextPropertyEditor;
import net.logicim.ui.property.FormPanel;

public class SimulationPropertiesPanel
    extends FormPanel
{
  public static final String TYPE_NAME = "Name";

  protected TextPropertyEditor name;

  public SimulationPropertiesPanel(String name)
  {
    super();
    this.name = new TextPropertyEditor(TYPE_NAME, name);
    addLabeledComponent(TYPE_NAME, this.name.getComponent());
  }

  public String getSimulationName()
  {
    return name.getValue();
  }
}

