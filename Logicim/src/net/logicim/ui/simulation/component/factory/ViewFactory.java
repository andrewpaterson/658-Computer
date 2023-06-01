package net.logicim.ui.simulation.component.factory;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.property.DefaultComponentProperties;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.property.ReflectivePropertyEditorDialog;
import net.logicim.ui.simulation.CircuitEditor;

import javax.swing.*;

public abstract class ViewFactory<STATIC extends StaticView<PROPERTIES>, PROPERTIES extends ComponentProperties>
{
  public abstract STATIC create(CircuitEditor circuitEditor, Int2D position, Rotation rotation);

  protected PROPERTIES createDefaultProperties(Class<STATIC> viewClass)
  {
    PROPERTIES defaultProperties = DefaultComponentProperties.get(viewClass);
    if (defaultProperties != null)
    {
      PROPERTIES properties = (PROPERTIES) defaultProperties.duplicate();
      properties.name = "";
      return properties;
    }
    else
    {
      return createInitialProperties();
    }
  }

  public abstract PROPERTIES createInitialProperties();

  public abstract STATIC create(CircuitEditor circuitEditor,
                                SubcircuitView subcircuitView,
                                Int2D position,
                                Rotation rotation,
                                PROPERTIES properties);

  public abstract Class<STATIC> getViewClass();

  public PropertyEditorDialog createEditorDialog(JFrame parentFrame, Logicim editor, StaticView<?> componentView)
  {
    return new ReflectivePropertyEditorDialog(parentFrame, componentView, editor);
  }
}

