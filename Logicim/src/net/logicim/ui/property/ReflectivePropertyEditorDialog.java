package net.logicim.ui.property;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

public class ReflectivePropertyEditorDialog
    extends PropertyEditorDialog
{
  public ReflectivePropertyEditorDialog(Frame owner, StaticView<?> componentView, SimulatorEditor editor)
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
  protected boolean updateProperties()
  {
    ReflectivePropertiesPanel reflectivePropertiesPanel = getReflectivePropertiesPanel();
    Map<Field, Object> map = reflectivePropertiesPanel.getProperties();

    boolean propertyChanged = updateRotation(reflectivePropertiesPanel);

    InstanceInspector instanceInspector = new InstanceInspector(componentView.getProperties());
    for (Map.Entry<Field, Object> entry : map.entrySet())
    {
      Field field = entry.getKey();
      Object newValue = entry.getValue();
      Object oldValue = instanceInspector.getFieldValue(field);

      if (!Objects.equals(newValue, oldValue))
      {
        propertyChanged = true;
        instanceInspector.setFieldValue(field, coerce(newValue, field.getType()));
      }
    }
    return propertyChanged;
  }

  private ReflectivePropertiesPanel getReflectivePropertiesPanel()
  {
    return (ReflectivePropertiesPanel) getPropertiesPanel();
  }
}

