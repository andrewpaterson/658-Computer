package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.ui.components.typeeditor.EnumPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

public class HorizontalAlignmentEditor
    extends EnumPropertyEditor<HorizontalAlignment>
{
  public HorizontalAlignmentEditor(PropertiesPanel propertiesPanel, String name, HorizontalAlignment current)
  {
    super(propertiesPanel, name, HorizontalAlignment.class, current);
  }
}

