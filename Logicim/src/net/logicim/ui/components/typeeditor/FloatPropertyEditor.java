package net.logicim.ui.components.typeeditor;

import net.logicim.ui.editor.InternationalUnits;

import javax.swing.*;

public class FloatPropertyEditor
    extends JTextField
    implements PropertyEditor
{
  protected String unit;

  public FloatPropertyEditor(double value, String unit)
  {
    super();
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
}
