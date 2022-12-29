package net.logicim.ui.components.typeeditor;

import net.logicim.common.SimulatorException;
import net.logicim.ui.editor.InternationalUnits;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TypeEditorFactory
{
  protected List<PropertyEditor> customEditors;

  protected static TypeEditorFactory instance;

  public TypeEditorFactory()
  {
    customEditors = new ArrayList<>();
  }

  public static TypeEditorFactory getInstance()
  {
    if (instance == null)
    {
      instance = new TypeEditorFactory();
    }
    return instance;
  }

  public JComponent createEditor(Class<?> fieldClass, String fieldName, Object fieldValue)
  {
    if (String.class.equals(fieldClass))
    {
      JTextField textField = new JTextField();
      textField.setText((String) fieldValue);
      return textField;
    }
    else if (Boolean.class.equals(fieldClass) || boolean.class.equals(fieldClass))
    {
      JCheckBox checkBox = new JCheckBox();
      checkBox.setSelected((Boolean) fieldValue);
      return checkBox;
    }
    else if (Float.class.equals(fieldClass) || float.class.equals(fieldClass))
    {
      int index = fieldName.indexOf('_');
      String unit = "";
      if (index != -1)
      {
        unit = fieldName.substring(index + 1);
      }

      JTextField textField = new JTextField();
      textField.setText(InternationalUnits.toString((float) fieldValue, unit));
      return textField;
    }
    else
    {
      for (PropertyEditor customEditor : customEditors)
      {
        if (customEditor.getPropertyClass().equals(fieldClass))
        {
          return customEditor.createEditor(fieldName, fieldValue);
        }
      }

      throw new SimulatorException("Cannot create typed editor for property class [%s].", fieldClass.getSimpleName());
    }
  }

  public void add(PropertyEditor propertyEditor)
  {
    customEditors.add(propertyEditor);
  }
}

