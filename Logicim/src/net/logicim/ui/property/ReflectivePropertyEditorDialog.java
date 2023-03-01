package net.logicim.ui.property;

import net.logicim.common.reflect.ClassInspector;
import net.logicim.common.reflect.InstanceInspector;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

public class ReflectivePropertyEditorDialog
    extends PropertyEditorDialog
{
  public ReflectivePropertyEditorDialog(Frame owner,
                                        StaticView<?> componentView,
                                        SimulatorEditor editor)
  {
    super(owner, componentView.getType() + " Properties", new Dimension(360, 320), editor, componentView);
  }

  @Override
  protected JPanel createEditorPanel()
  {
    return new ReflectivePropertiesPanel(componentView);
  }

  protected Object coerce(Object value, Class<?> type)
  {
    if (value == null)
    {
      return null;
    }

    if (value instanceof Long)
    {
      if (type.equals(Integer.class) || type.equals(int.class))
      {
        return ((Long) value).intValue();
      }
    }

    if (value instanceof Double)
    {
      if (type.equals(Float.class) || type.equals(float.class))
      {
        return ((Double) value).floatValue();
      }
    }

    if (value instanceof Integer)
    {
      if (type.equals(Long.class) || type.equals(long.class))
      {
        return ((Integer) value).longValue();
      }
    }

    if (value instanceof Float)
    {
      if (type.equals(Double.class) || type.equals(double.class))
      {
        return ((Float) value).doubleValue();
      }
    }

    return value;
  }

  @Override
  protected ComponentProperties updateProperties()
  {
    ReflectivePropertiesPanel reflectivePropertiesPanel = getReflectivePropertiesPanel();
    Map<Field, Object> map = reflectivePropertiesPanel.getProperties();

    boolean propertyChanged = updateRotation(reflectivePropertiesPanel);

    ComponentProperties oldProperties = componentView.getProperties();
    ClassInspector classInspector = ClassInspector.forClass(oldProperties.getClass());
    ComponentProperties newProperties = (ComponentProperties) classInspector.newInstance();

    InstanceInspector oldInspector = new InstanceInspector(oldProperties);
    InstanceInspector newInspector = new InstanceInspector(newProperties);
    for (Map.Entry<Field, Object> entry : map.entrySet())
    {
      Field field = entry.getKey();
      Object newValue = entry.getValue();
      Object oldValue = oldInspector.getFieldValue(field);

      if (!Objects.equals(newValue, oldValue))
      {
        propertyChanged = true;
      }
      newInspector.setFieldValue(field, coerce(newValue, field.getType()));
    }
    if (propertyChanged)
    {
      return newProperties;
    }
    else
    {
      return null;
    }
  }

  private ReflectivePropertiesPanel getReflectivePropertiesPanel()
  {
    return (ReflectivePropertiesPanel) getPropertiesPanel();
  }
}

