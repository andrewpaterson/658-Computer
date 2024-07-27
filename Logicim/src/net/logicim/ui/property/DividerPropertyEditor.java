package net.logicim.ui.property;

import net.logicim.ui.components.typeeditor.PropertyEditor;

import javax.swing.*;

public class DividerPropertyEditor
    extends JLabel
    implements PropertyEditor
{
  public DividerPropertyEditor(String name)
  {
    super(FieldNameHelper.calculateHumanReadableName(name));
    setName(name);
  }

  @Override
  public Object getValue()
  {
    return null;
  }

  @Override
  public JComponent getComponent()
  {
    return this;
  }

  @Override
  public boolean isDivider()
  {
    return true;
  }
}

