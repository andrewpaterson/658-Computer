package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;

public interface PropertyEditor
{
  Object getValue();

  JComponent getComponent();

  PropertiesPanel getPropertiesPanel();

  default boolean isDivider()
  {
    return false;
  }
}

