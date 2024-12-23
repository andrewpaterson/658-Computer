package net.logicim.ui.components.typeeditor;

import net.logicim.data.family.Family;
import net.logicim.ui.property.PropertiesPanel;

public class FamilyPropertyEditorFactory
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
    return new FamilyPropertyEditor(fieldName, (Family) fieldValue);
  }
}

