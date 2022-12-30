package net.logicim.ui.components.typeeditor;

import net.logicim.common.SimulatorException;

import java.util.ArrayList;
import java.util.List;

public class TypeEditorFactory
{
  protected List<PropertyEditorFactory> customEditors;

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

  public PropertyEditor createEditor(Class<?> fieldClass, String fieldName, Object fieldValue)
  {
    if (String.class.equals(fieldClass))
    {
      return new TextPropertyEditor((String) fieldValue);
    }
    else if (Boolean.class.equals(fieldClass) || boolean.class.equals(fieldClass))
    {
      return new BooleanPropertyEditor((Boolean) fieldValue);
    }
    else if (Float.class.equals(fieldClass) || float.class.equals(fieldClass))
    {
      return new FloatPropertyEditor((float) fieldValue, getUnit(fieldName));
    }
    else
    {
      for (PropertyEditorFactory customEditor : customEditors)
      {
        if (customEditor.getPropertyClass().equals(fieldClass))
        {
          return customEditor.createEditor(fieldName, fieldValue);
        }
      }

      throw new SimulatorException("Cannot create typed editor for property class [%s].", fieldClass.getSimpleName());
    }
  }

  protected String getUnit(String fieldName)
  {
    int index = fieldName.indexOf('_');
    String unit = "";
    if (index != -1)
    {
      unit = fieldName.substring(index + 1);
    }
    return unit;
  }

  public void add(PropertyEditorFactory propertyEditorFactory)
  {
    customEditors.add(propertyEditorFactory);
  }

  public void addAll(PropertyEditorFactory ... propertyEditorFactories)
  {
    for (PropertyEditorFactory propertyEditorFactory : propertyEditorFactories)
    {
      customEditors.add(propertyEditorFactory);
    }
  }
}

