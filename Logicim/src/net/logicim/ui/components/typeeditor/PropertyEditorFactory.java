package net.logicim.ui.components.typeeditor;

public abstract class PropertyEditorFactory
{
  public abstract Class<?> getPropertyClass();

  public abstract PropertyEditor createEditor(String fieldName, Object fieldValue);
}

