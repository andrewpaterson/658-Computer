package net.logicim.ui.circuit;

import net.logicim.ui.common.subcircuit.SubcircuitInstanceProperties;
import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.TextPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

import java.awt.*;

public class SubcircuitPropertiesPanel
    extends PropertiesPanel
{
  public static final String NAME = "Name";

  protected TextPropertyEditor name;

  public SubcircuitPropertiesPanel(String name)
  {
    super(new GridBagLayout());
    Form form = new Form();
    this.name = new TextPropertyEditor(this, NAME, name);
    form.addComponents(new Label(NAME), this.name.getComponent());

    addPropertyFormView(form);
  }

  public String getSubcircuitName()
  {
    return name.getValue();
  }
}

