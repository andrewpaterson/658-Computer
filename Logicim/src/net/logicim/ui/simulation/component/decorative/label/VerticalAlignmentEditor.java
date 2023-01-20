package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.data.integratedcircuit.decorative.VerticalAlignment;
import net.logicim.ui.components.typeeditor.EnumPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

public class VerticalAlignmentEditor
    extends EnumPropertyEditor<VerticalAlignment>
{
  public VerticalAlignmentEditor(PropertiesPanel propertiesPanel, String name, VerticalAlignment current)
  {
    super(propertiesPanel, name, VerticalAlignment.class, current);
  }
}

