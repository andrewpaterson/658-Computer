package net.logicim.common.reflect;

import net.logicim.common.SimulatorException;

import java.lang.reflect.Field;
import java.util.Collection;

public class InstanceInspector
{
  protected Object instance;
  protected ClassInspector classInspector;

  public InstanceInspector(Object instance)
  {
    this.instance = instance;
    classInspector = ClassInspector.forClass(instance.getClass());
  }

  public Object getFieldValue(String fieldName)
  {
    int index = fieldName.indexOf('.');
    Field field = null;
    if (index == -1)
    {
      field = classInspector.getField(fieldName);
    }
    else
    {
      String start = fieldName.substring(0, index);
      String end = fieldName.substring(index + 1);
      InstanceInspector instanceInspector = new InstanceInspector(getFieldValue(start));
      return instanceInspector.getFieldValue(end);
    }

    return getFieldValue(field);
  }

  public Object getFieldValue(Field field)
  {
    try
    {
      return field.get(instance);
    }
    catch (IllegalAccessException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public Field getField(String fieldName)
  {
    return classInspector.getField(fieldName);
  }

  public void setFieldValue(String fieldName, Object value)
  {
    int index = fieldName.indexOf('.');
    Field field;
    if (index == -1)
    {
      field = classInspector.getField(fieldName);
    }
    else
    {
      String start = fieldName.substring(0, index);
      String end = fieldName.substring(index + 1);
      InstanceInspector instanceInspector = new InstanceInspector(getFieldValue(start));
      instanceInspector.setFieldValue(end, value);
      return;
    }

    setFieldValue(field, value);
  }

  public void setFieldValue(Field field, Object value)
  {
    try
    {
      field.set(instance, value);
    }
    catch (IllegalAccessException | IllegalArgumentException e)
    {
      throw new SimulatorException(e.getMessage());
    }
  }

  public Object getInstance()
  {
    return instance;
  }

  public ClassInspector getClassInspector()
  {
    return classInspector;
  }

  public Collection<String> getFieldNames()
  {
    return classInspector.getAllInstanceFieldNames();
  }

  public Collection<Field> getFields()
  {
    return classInspector.getAllFields();
  }

  public Class<?> getFieldClass(String fieldName)
  {
    Field field = getField(fieldName);
    return field.getType();
  }
}

