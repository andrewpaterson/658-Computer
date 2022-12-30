package net.logicim.ui.components.typeeditor;

import net.logicim.ui.editor.InternationalUnits;

import javax.swing.*;

public class IntegerPropertyEditor
    extends JTextField
    implements PropertyEditor
{
  public IntegerPropertyEditor(int value)
  {
    super();
    setText(Integer.toString(value));
  }

  @Override
  public Object getValue()
  {
    return Integer.parseInt(getText());
  }

  @Override
  public JComponent getComponent()
  {
    return this;
  }
}

