package net.logicim.ui.components.typeeditor;

import javax.swing.*;

public class BooleanPropertyEditor
    extends JCheckBox
    implements PropertyEditor
{
  public BooleanPropertyEditor(Boolean selected)
  {
    super();
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

