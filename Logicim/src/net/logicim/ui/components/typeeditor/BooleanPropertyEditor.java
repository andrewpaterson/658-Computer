package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;

public class BooleanPropertyEditor
    extends JCheckBox
    implements PropertyEditor
{
  public BooleanPropertyEditor(String name, Boolean selected)
  {
    super();
    setName(name);
    setSelected(selected);
  }

  @Override
  public Object getValue()
  {
    return isSelected();
  }

  @Override
  public JComponent getComponent()
  {
    return this;
  }
}

