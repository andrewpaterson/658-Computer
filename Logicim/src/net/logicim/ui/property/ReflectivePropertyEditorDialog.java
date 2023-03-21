package net.logicim.ui.property;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.ui.Logicim;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;

import javax.swing.*;
import java.awt.*;

public class ReflectivePropertyEditorDialog
    extends PropertyEditorDialog
{
  public ReflectivePropertyEditorDialog(Frame owner,
                                        StaticView<?> componentView,
                                        Logicim editor)
  {
    super(owner,
          componentView.getType() + " Properties",
          new Dimension(DEFAULT_PROPERTY_DIALOG_WIDTH, getPropertiesFieldCount(componentView) * 26 + 32 + 32 + 32),
          editor,
          (StaticView) componentView);
  }

  public static int getPropertiesFieldCount(StaticView<?> componentView)
  {
    ComponentProperties properties = componentView.getProperties();

    InstanceInspector instanceInspector = new InstanceInspector(properties);
    return instanceInspector.getFields().size();
  }

  @Override
  protected JPanel createEditorPanel()
  {
    return new ReflectivePropertiesPanel(componentView);
  }

  @Override
  protected ComponentProperties updateProperties()
  {
    ReflectivePropertiesPanel reflectivePropertiesPanel = getReflectivePropertiesPanel();
    boolean propertyChanged = updateRotation(reflectivePropertiesPanel);

    ComponentProperties oldProperties = componentView.getProperties();
    ComponentProperties newProperties = reflectivePropertiesPanel.createProperties(oldProperties);
    propertyChanged |= !oldProperties.equals(newProperties);
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

