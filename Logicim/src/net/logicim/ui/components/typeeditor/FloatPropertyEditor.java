package net.logicim.ui.components.typeeditor;

import net.logicim.ui.editor.InternationalUnits;
import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;

public class FloatPropertyEditor
    extends JTextField
    implements PropertyEditor
{
  protected PropertiesPanel propertiesPanel;
  protected String unit;

  public FloatPropertyEditor(PropertiesPanel propertiesPanel, String name, double value, String unit)
  {
    super();
    setName(name);
    this.propertiesPanel = propertiesPanel;

    this.unit = unit;
    setText(InternationalUnits.toString(value, unit));
  }

  @Override
  public Object getValue()
  {
    return InternationalUnits.parse(getText(), unit);
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

