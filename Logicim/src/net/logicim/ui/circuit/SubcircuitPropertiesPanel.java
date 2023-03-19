package net.logicim.ui.circuit;

import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.TextPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

import java.awt.*;

public class SubcircuitPropertiesPanel
    extends PropertiesPanel
{
  public static final String TYPE_NAME = "Name";

  protected TextPropertyEditor name;

  public SubcircuitPropertiesPanel(String name)
  {
    super(new GridBagLayout());
    Form form = new Form();
    this.name = new TextPropertyEditor(this, TYPE_NAME, name);
    form.addComponents(new Label(TYPE_NAME), this.name.getComponent());

    addPropertyFormView(form);
  }

  public String getSubcircuitName()
  {
    return name.getValue();
  }
}

