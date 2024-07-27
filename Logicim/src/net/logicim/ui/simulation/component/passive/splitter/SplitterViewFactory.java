package net.logicim.ui.simulation.component.passive.splitter;

import net.common.type.Int2D;
import net.logicim.data.passive.wire.SplitterAppearance;
import net.logicim.data.passive.wire.SplitterProperties;
import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

import javax.swing.*;

public class SplitterViewFactory
    extends ViewFactory<SplitterView, SplitterProperties>
{
  @Override
  public SplitterView create(CircuitEditor circuitEditor,
                             Int2D position,
                             Rotation rotation)
  {
    return create(
        circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
        rotation,
        createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true));
  }

  @Override
  public SplitterProperties createInitialProperties()
  {
    return new SplitterProperties(null,
                                  2,
                                  2,
                                  2,
                                  SplitterAppearance.LEFT_HANDED,
                                  -1);
  }

  @Override
  public SplitterView create(CircuitEditor circuitEditor,
                             SubcircuitView subcircuitView,
                             Int2D position,
                             Rotation rotation,
                             SplitterProperties properties)
  {
    return new SplitterView(subcircuitView,
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
  public PropertyEditorDialog createEditorDialog(JFrame parentFrame, Logicim editor, StaticView<?> componentView)
  {
    return new SplitterPropertyEditorDialog(parentFrame, editor, (SplitterView) componentView);
  }
}

