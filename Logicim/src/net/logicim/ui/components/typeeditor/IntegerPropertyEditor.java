package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;

public class IntegerPropertyEditor
    extends JTextField
    implements PropertyEditor
{
  private PropertiesPanel propertiesPanel;

  public IntegerPropertyEditor(PropertiesPanel propertiesPanel, String name, int value)
  {
    super();
    setName(name);
    this.propertiesPanel = propertiesPanel;

    setText(Integer.toString(value));
    addFocusListener(propertiesPanel);
    addActionListener(propertiesPanel);
  }

  @Override
  public Integer getValue()
  {
    return Integer.parseInt(getText());
  }

  @Override
  public JComponent getComponent()
  {
    return this;
  }

  public PropertiesPanel getPropertiesPanel()
  {
    return propertiesPanel;
  }
}

