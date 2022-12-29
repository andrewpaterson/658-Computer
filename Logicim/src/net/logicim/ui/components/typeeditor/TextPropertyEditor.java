package net.logicim.ui.components.typeeditor;

import javax.swing.*;

public class TextPropertyEditor
    extends JTextField
    implements PropertyEditor
{
  public TextPropertyEditor(String text)
  {
    super(text);
  }

  @Override
  public Object getValue()
  {
    return getText();
  }

  @Override
  public JComponent getComponent()
  {
    return this;
  }
}

