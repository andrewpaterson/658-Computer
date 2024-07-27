package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

public abstract class PropertyEditorFactory
{
  public abstract Class<?> getPropertyClass();

  public abstract PropertyEditor createEditor(PropertiesPanel propertiesPanel, String fieldName, Object fieldValue);
}

