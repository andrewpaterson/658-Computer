package net.logicim.ui.components.typeeditor;

import javax.swing.*;

public abstract class PropertyEditor
{
  public abstract Class<?> getPropertyClass();

  public abstract JComponent createEditor(String fieldName, Object fieldValue);
}

