package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;

public class IntegerPropertyEditor
    extends JTextField
    implements PropertyEditor
{
  public IntegerPropertyEditor(PropertiesPanel propertiesPanel, String name, int value)
  {
    super();
    setName(name);

    setText(Integer.toString(value));
    addFocusListener(propertiesPanel);
    addActionListener(propertiesPanel);
  }

  @Override
  public Integer getValue()
  {
    try
    {
      return Integer.parseInt(getText());
    }
    catch (NumberFormatException e)
    {
      setText("0");
      return 0;
    }
  }

  @Override
  public JComponent getComponent()
  {
    return this;
  }
}

