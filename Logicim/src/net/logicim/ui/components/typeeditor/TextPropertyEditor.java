package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;

public class TextPropertyEditor
    extends JTextField
    implements PropertyEditor
{
  protected PropertiesPanel propertiesPanel;

  public TextPropertyEditor(PropertiesPanel propertiesPanel, String name, String text)
  {
    super(text);
    setName(name);
    this.propertiesPanel = propertiesPanel;
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

