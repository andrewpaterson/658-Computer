package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.common.type.Int2D;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

import javax.swing.*;

public class SplitterViewFactory
    extends ViewFactory<SplitterView, SplitterProperties>
{
  @Override
  public SplitterView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new SplitterView(circuitEditor,
                            position,
                            rotation,
                            new SplitterProperties("", 2, 2, -1, 2));
  }

  @Override
  public SplitterView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, SplitterProperties properties)
  {
    return new SplitterView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<SplitterView> getViewClass()
  {
    return SplitterView.class;
  }

  @Override
  public PropertyEditorDialog createEditorDialog(JFrame parentFrame, SimulatorEditor editor, ComponentView<?> componentView)
  {
    return new SplitterPropertyEditorDialog(parentFrame, editor, componentView);
  }
}

