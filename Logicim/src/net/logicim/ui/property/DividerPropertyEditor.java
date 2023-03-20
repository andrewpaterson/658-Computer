package net.logicim.ui.property;

import net.logicim.ui.components.typeeditor.PropertyEditor;

import javax.swing.*;

public class DividerPropertyEditor
    extends JLabel
    implements PropertyEditor
{
  protected PropertiesPanel propertiesPanel;

  public DividerPropertyEditor(PropertiesPanel propertiesPanel, String name)
  {
    super(FieldNameHelper.calculateHumanReadableName(name));
    setName(name);
    this.propertiesPanel = propertiesPanel;
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
  public PropertiesPanel getPropertiesPanel()
  {
    return propertiesPanel;
  }

  @Override
  public boolean isDivider()
  {
    return true;
  }
}

