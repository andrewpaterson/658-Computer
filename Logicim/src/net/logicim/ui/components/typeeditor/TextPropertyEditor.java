package net.logicim.ui.components.typeeditor;

import javax.swing.*;

public class TextPropertyEditor
    extends JTextField
    implements PropertyEditor
{

  public TextPropertyEditor(String name, String text)
  {
    super(calculateText(text));
    setName(name);
  }

  protected static String calculateText(String text)
  {
    if (text != null)
    {
      return text.trim();
    }
    else
    {
      return "";
    }
  }

  @Override
  public String getValue()
  {
    return getText().trim();
  }

  @Override
  public JComponent getComponent()
  {
    return this;
  }
}

