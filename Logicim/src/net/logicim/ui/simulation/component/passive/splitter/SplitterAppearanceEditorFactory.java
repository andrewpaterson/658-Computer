package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.data.passive.wire.SplitterAppearance;
import net.logicim.domain.common.propagation.Family;
import net.logicim.ui.components.typeeditor.PropertyEditor;
import net.logicim.ui.components.typeeditor.PropertyEditorFactory;
import net.logicim.ui.property.PropertiesPanel;

public class SplitterAppearanceEditorFactory
    extends PropertyEditorFactory
{
  @Override
  public Class<?> getPropertyClass()
  {
    return Family.class;
  }

  @Override
  public PropertyEditor createEditor(PropertiesPanel propertiesPanel, String fieldName, Object fieldValue)
  {
    return new SplitterAppearanceEditor(propertiesPanel, fieldName, (SplitterAppearance) fieldValue);
  }
}

