package net.logicim.ui.editor;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.ui.common.integratedcircuit.DiscreteProperties;
import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.TypeEditorFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.VERTICAL;
import static net.logicim.common.util.StringUtil.javaNameToHumanReadable;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class PropertiesPanel
    extends JPanel
{
  public PropertiesPanel(DiscreteProperties properties)
  {
    super(new GridBagLayout());

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

      form.addComponents(new Label(name), TypeEditorFactory.getInstance().createEditor(field.getType(), fieldName, fieldValue));
    }

    add(form, gridBagConstraints(0, 0, 1, 0, HORIZONTAL));
    add(new JPanel(), gridBagConstraints(0, 1, 0, 1, VERTICAL));
    setBorder(new EmptyBorder(5, 5, 0, 5));
  }
}

