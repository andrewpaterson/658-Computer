package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.common.type.Int2D;
import net.logicim.data.passive.wire.SplitterAppearance;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.simulation.component.factory.ViewFactory;

import javax.swing.*;

public class SplitterViewFactory
    extends ViewFactory<SplitterView, SplitterProperties>
{
  @Override
  public SplitterView create(SubcircuitView subcircuitView,
                             Circuit circuit,
                             Int2D position,
                             Rotation rotation)
  {
    return new SplitterView(subcircuitView,
                            circuit,
                            position,
                            rotation,
                            new SplitterProperties(null,
                                                   2,
                                                   2,
                                                   2,
                                                   SplitterAppearance.LEFT_HANDED,
                                                   -1));
  }

  @Override
  public SplitterView create(SubcircuitView subcircuitView,
                             Circuit circuit,
                             Int2D position,
                             Rotation rotation,
                             SplitterProperties properties)
  {
    return new SplitterView(subcircuitView,
                            circuit,
                            position,
                            rotation,
                            properties);
  }

  @Override
  public Class<SplitterView> getViewClass()
  {
    return SplitterView.class;
  }

  @Override
  public PropertyEditorDialog createEditorDialog(JFrame parentFrame, SimulatorEditor editor, StaticView<?> componentView)
  {
    return new SplitterPropertyEditorDialog(parentFrame, editor, (SplitterView) componentView);
  }
}

