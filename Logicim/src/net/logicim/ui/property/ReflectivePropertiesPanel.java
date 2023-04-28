package net.logicim.ui.property;

import net.logicim.common.reflect.ClassInspector;
import net.logicim.common.reflect.InstanceInspector;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.PropertyEditor;
import net.logicim.ui.components.typeeditor.TypeEditorFactory;
import net.logicim.ui.util.SeparatorUtil;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.logicim.ui.property.FieldNameHelper.calculateHumanReadableName;

public class ReflectivePropertiesPanel
    extends PropertiesPanel
    implements RotationEditorHolder
{
  protected Map<Field, PropertyEditor> fieldProperties;
  protected RotationEditor rotationEditor;

  public ReflectivePropertiesPanel(StaticView<?> componentView)
  {
    super(new GridBagLayout());
    ComponentProperties properties = componentView.getProperties();

    fieldProperties = new LinkedHashMap<>();

    InstanceInspector instanceInspector = new InstanceInspector(properties);
    List<Field> fields = new ArrayList<>(instanceInspector.getFields());

    Form form = new Form();
    rotationEditor = new RotationEditor(this, "rotation", componentView.getRotation());
    form.addComponents(new Label("Rotation"), rotationEditor.getComponent());
    for (Field field : fields)
    {
      Class<?> fieldType = field.getType();
      Object fieldValue = instanceInspector.getFieldValue(field);
      String fieldName = field.getName();
      String name = calculateHumanReadableName(fieldName);

      PropertyEditor propertyEditor = TypeEditorFactory.getInstance().createEditor(this, fieldType, fieldName, fieldValue);
      if (!propertyEditor.isDivider())
      {
        form.addComponents(new Label(name), propertyEditor.getComponent());
      }
      else
      {
        if (!form.isFirst())
        {
          form.addVerticalSpacer(10);
        }

        form.addComponent(SeparatorUtil.buildSeparator(propertyEditor.getComponent()));
      }
      fieldProperties.put(field, propertyEditor);
    }

    addPropertyFormView(form);
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
  public ComponentProperties createProperties(ComponentProperties oldProperties)
  {
    Map<Field, Object> map = getProperties();

    ClassInspector classInspector = ClassInspector.forClass(oldProperties.getClass());
    ComponentProperties newProperties = (ComponentProperties) classInspector.newInstance();

    InstanceInspector newInspector = new InstanceInspector(newProperties);
    for (Map.Entry<Field, Object> entry : map.entrySet())
    {
      Field field = entry.getKey();
      Object newValue = entry.getValue();
      newInspector.setFieldValue(field, coerce(newValue, field.getType()));
    }
    return newProperties;
  }

  @Override
  public Rotation getRotation()
  {
    return rotationEditor.getValue();
  }
}

