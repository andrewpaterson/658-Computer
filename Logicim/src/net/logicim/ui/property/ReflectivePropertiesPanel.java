package net.logicim.ui.property;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.PropertyEditor;
import net.logicim.ui.components.typeeditor.TypeEditorFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.VERTICAL;
import static net.logicim.common.util.StringUtil.javaNameToHumanReadable;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class ReflectivePropertiesPanel
    extends JPanel
{
  protected Map<Field, PropertyEditor> fieldProperties;

  public ReflectivePropertiesPanel(ComponentProperties properties)
  {
    super(new GridBagLayout());

    fieldProperties = new LinkedHashMap<>();

    InstanceInspector instanceInspector = new InstanceInspector(properties);
    List<Field> fields = new ArrayList<>(instanceInspector.getFields());
    Collections.reverse(fields);

    Form form = new Form();
    for (Field field : fields)
    {
      Object fieldValue = instanceInspector.getFieldValue(field);
      String fieldName = field.getName();
      int index = fieldName.indexOf('_');
      String name = fieldName;
      if (index != -1)
      {
        name = fieldName.substring(0, index);
      }
      name = javaNameToHumanReadable(name);

      PropertyEditor component = TypeEditorFactory.getInstance().createEditor(field.getType(), fieldName, fieldValue);
      form.addComponents(new Label(name), component.getComponent());
      fieldProperties.put(field, component);
    }

    add(form, gridBagConstraints(0, 0, 1, 0, HORIZONTAL));
    add(new JPanel(), gridBagConstraints(0, 1, 0, 1, VERTICAL));
    setBorder(new EmptyBorder(5, 5, 0, 5));
  }

  public Map<Field, Object> getProperties()
  {
    LinkedHashMap<Field, Object> hashMap = new LinkedHashMap<>();
    for (Field field : fieldProperties.keySet())
    {
      PropertyEditor propertyEditor = fieldProperties.get(field);
      hashMap.put(field, propertyEditor.getValue());
    }
    return hashMap;
  }
}

