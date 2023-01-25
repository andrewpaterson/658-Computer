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
    super(calculateText(text));
    setName(name);
    this.propertiesPanel = propertiesPanel;
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

  @Override
  public PropertiesPanel getPropertiesPanel()
  {
    return propertiesPanel;
  }
}

