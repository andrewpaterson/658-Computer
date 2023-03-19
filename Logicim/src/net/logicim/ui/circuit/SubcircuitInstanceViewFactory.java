package net.logicim.ui.circuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.Logicim;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.subcircuit.SubcircuitInstanceProperties;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import javax.swing.*;

public class SubcircuitInstanceViewFactory
    extends ViewFactory<SubcircuitInstanceView, SubcircuitInstanceProperties>
{
  @Override
  public SubcircuitInstanceView create(CircuitEditor circuitEditor,
                                       Int2D position,
                                       Rotation rotation)
  {
    throw new SimulatorException("Cannot call SubcircuitViewFactory.create().  Use startPlaceSubcircuit() instead.");
  }

  @Override
  public SubcircuitInstanceView create(CircuitEditor circuitEditor, Int2D position,
                                       Rotation rotation,
                                       SubcircuitInstanceProperties properties)
  {
    SubcircuitView subcircuitView = circuitEditor.getCurrentSubcircuitView();
    Circuit circuit = circuitEditor.getCircuit();
    SubcircuitEditor instanceSubcircuitEditor = circuitEditor.getSubcircuitEditor(properties.subcircuitTypeName);
    return new SubcircuitInstanceView(subcircuitView,
                                      instanceSubcircuitEditor.getSubcircuitView(),
                                      circuit,
                                      position,
                                      rotation);
  }

  @Override
  public Class<SubcircuitInstanceView> getViewClass()
  {
    return SubcircuitInstanceView.class;
  }

  @Override
  public PropertyEditorDialog createEditorDialog(JFrame parentFrame, Logicim editor, StaticView<?> componentView)
  {
    return new SubcircuitInstancePropertyEditorDialog(parentFrame, editor, (SubcircuitInstanceView) componentView);
  }
}

