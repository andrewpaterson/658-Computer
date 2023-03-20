package net.logicim.ui.property;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.PropertyEditor;
import net.logicim.ui.components.typeeditor.TypeEditorFactory;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;

import static net.logicim.common.util.StringUtil.javaNameToHumanReadable;

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
      Object fieldValue = instanceInspector.getFieldValue(field);
      String fieldName = field.getName();
      int index = fieldName.indexOf('_');
      String name = fieldName;
      if (index != -1)
      {
        name = fieldName.substring(0, index);
      }
      name = javaNameToHumanReadable(name);

      PropertyEditor propertyEditor = TypeEditorFactory.getInstance().createEditor(this, field.getType(), fieldName, fieldValue);
      form.addComponents(new Label(name), propertyEditor.getComponent());
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

  @Override
  public Rotation getRotation()
  {
    return rotationEditor.getValue();
  }
}

