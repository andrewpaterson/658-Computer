package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.IntegerPropertyEditor;
import net.logicim.ui.components.typeeditor.PropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class SplitterPropertiesPanel
    extends PropertiesPanel
{
  Map<String, PropertyEditor> propertyEditors;

  public SplitterPropertiesPanel(SplitterProperties properties)
  {
    super(new GridBagLayout());

    propertyEditors = new LinkedHashMap<>();

    add("Fan Out", properties.fanOut);
    add("Bit Width", properties.bitWidth);
    add("Appearance", properties.fanOut);
    add("Spacing", properties.gridSpacing);
    add("Offset", properties.endOffset);

    Form form = new Form();
    Set<String> keySet = propertyEditors.keySet();
    Iterator<String> iterator = keySet.iterator();
    for (int i = 0; i < keySet.size(); i++)
    {
      String name = iterator.next();
      PropertyEditor propertyEditor = propertyEditors.get(name);
      int verticalSpacing = 2;
      if (i == keySet.size() - 1)
      {
        verticalSpacing = 5;
      }
      form.addComponents(new Label(name), propertyEditor.getComponent(), verticalSpacing);
    }

    for (int i = 0; i < properties.bitWidth; i++)
    {
      String name = "Bit " + i;
      IntegerPropertyEditor bitEditor = add(name, 0);
      form.addComponents(new Label(name), bitEditor.getComponent(), 0);
    }

    addPropertyFormView(form);
  }

  protected IntegerPropertyEditor add(String name, int value)
  {
    IntegerPropertyEditor propertyEditor = new IntegerPropertyEditor(this, name, value);
    propertyEditors.put(name, propertyEditor);
    return propertyEditor;
  }
}

