package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;

public class BooleanPropertyEditor
    extends JCheckBox
    implements PropertyEditor
{
  protected PropertiesPanel propertiesPanel;

  public BooleanPropertyEditor(PropertiesPanel propertiesPanel, String name, Boolean selected)
  {
    super();
    setName(name);
    this.propertiesPanel = propertiesPanel;

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

