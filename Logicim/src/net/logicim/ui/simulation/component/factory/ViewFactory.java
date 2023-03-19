package net.logicim.ui.simulation.component.factory;

import net.logicim.common.type.Int2D;
import net.logicim.ui.Logicim;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.property.ReflectivePropertyEditorDialog;
import net.logicim.ui.simulation.CircuitEditor;

import javax.swing.*;

public abstract class ViewFactory<DISCRETE extends StaticView<PROPERTIES>, PROPERTIES extends ComponentProperties>
{
  public abstract DISCRETE create(CircuitEditor circuitEditor, Int2D position, Rotation rotation);

  public abstract DISCRETE create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, PROPERTIES properties);

  public abstract Class<DISCRETE> getViewClass();

  public PropertyEditorDialog createEditorDialog(JFrame parentFrame, Logicim editor, StaticView<?> componentView)
  {
    return new ReflectivePropertyEditorDialog(parentFrame, componentView, editor);
  }
}

