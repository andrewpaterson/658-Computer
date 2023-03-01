package net.logicim.ui.simulation.component.factory;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.property.ReflectivePropertyEditorDialog;

import javax.swing.*;

public abstract class ViewFactory<DISCRETE extends StaticView<PROPERTIES>, PROPERTIES extends ComponentProperties>
{
  public abstract DISCRETE create(SubcircuitView subcircuitView, Circuit circuit, Int2D position, Rotation rotation);

  public abstract DISCRETE create(SubcircuitView subcircuitView, Circuit circuit, Int2D position, Rotation rotation, PROPERTIES properties);

  public abstract Class<DISCRETE> getViewClass();

  public PropertyEditorDialog createEditorDialog(JFrame parentFrame, SimulatorEditor editor, StaticView<?> componentView)
  {
    return new ReflectivePropertyEditorDialog(parentFrame, componentView, editor);
  }
}

