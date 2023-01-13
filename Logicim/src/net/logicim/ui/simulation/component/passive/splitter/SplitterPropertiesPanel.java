package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.IntegerPropertyEditor;
import net.logicim.ui.components.typeeditor.PropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class SplitterPropertiesPanel
    extends PropertiesPanel
{
  Map<String, PropertyEditor> propertyEditors;

  public SplitterPropertiesPanel(SplitterProperties properties)
  {
    super(new GridBagLayout());

    propertyEditors = new LinkedHashMap<>();

    propertyEditors.put("Fan Out", new IntegerPropertyEditor(properties.fanOut));
    propertyEditors.put("Bit Width", new IntegerPropertyEditor(properties.bitWidth));
    propertyEditors.put("Appearance", new IntegerPropertyEditor(properties.fanOut));
    propertyEditors.put("Spacing", new IntegerPropertyEditor(properties.gridSpacing));
    propertyEditors.put("Offset", new IntegerPropertyEditor(properties.endOffset));

    Form form = new Form();
    for (String name : propertyEditors.keySet())
    {
      PropertyEditor propertyEditor = propertyEditors.get(name);
      form.addComponents(new Label(name), propertyEditor.getComponent());
    }

    for (int i = 0; i < properties.bitWidth; i++)
    {
      IntegerPropertyEditor bitEditor = new IntegerPropertyEditor(i);
      String name = "Bit " + i;
      propertyEditors.put(name, bitEditor);
      form.addComponents(new Label(name), bitEditor.getComponent(), 0);
    }

    addPropertyFormView(form);
  }
}

