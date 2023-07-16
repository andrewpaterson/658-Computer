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
import net.logicim.ui.simulation.component.passive.pin.ComponentNameHelper;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import javax.swing.*;
import java.util.Set;

public abstract class ViewFactory<STATIC extends StaticView<PROPERTIES>, PROPERTIES extends ComponentProperties>
{
  protected PROPERTIES createDefaultProperties(SubcircuitEditor subcircuitEditor, Class<STATIC> viewClass, boolean updateName)
  {
    PROPERTIES defaultProperties = DefaultComponentProperties.get(viewClass);
    if (defaultProperties != null)
    {
      PROPERTIES properties = (PROPERTIES) defaultProperties.duplicate();
      if (updateName)
      {
        Set<StaticView<?>> staticViews = subcircuitEditor.findAllViewsOfClass(viewClass);
        properties.name = new ComponentNameHelper().getUniqueName(staticViews, properties.name);
      }
      return properties;
    }
    else
    {
      return createInitialProperties();
    }
  }

  public abstract STATIC create(CircuitEditor circuitEditor, Int2D position, Rotation rotation);

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

