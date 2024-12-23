package net.logicim.ui.simulation.component.decorative.label;

import net.common.type.Int2D;
import net.logicim.data.decorative.label.LabelProperties;
import net.logicim.data.integratedcircuit.decorative.HorizontalAlignment;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class LabelViewFactory
    extends ViewFactory<LabelView, LabelProperties>
{
  @Override
  public LabelView create(CircuitEditor circuitEditor,
                          Int2D position,
                          Rotation rotation)
  {
    return create(
        circuitEditor, circuitEditor.getCurrentSubcircuitView(), position,
        rotation,
        createDefaultProperties(circuitEditor.getCurrentSubcircuitEditor(), getViewClass(), true));
  }

  @Override
  public LabelProperties createInitialProperties()
  {
    return new LabelProperties("  ",
                               HorizontalAlignment.LEFT,
                               false,
                               true,
                               true);
  }

  @Override
  public LabelView create(CircuitEditor circuitEditor,
                          SubcircuitView subcircuitView,
                          Int2D position,
                          Rotation rotation,
                          LabelProperties properties)
  {
    return new LabelView(subcircuitView,
                         position,
                         rotation,
                         properties);
  }

  @Override
  public Class<LabelView> getViewClass()
  {
    return LabelView.class;
  }
}

