package net.logicim.ui.simulation.component.decorative.label;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;

public class LabelViewFactory
    extends ViewFactory<LabelView, LabelProperties>
{
  @Override
  public LabelView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new LabelView(circuitEditor, position, rotation, new LabelProperties("Hello"));
  }

  @Override
  public LabelView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, LabelProperties properties)
  {
    return new LabelView(circuitEditor, position, rotation, properties);
  }

  @Override
  public Class<LabelView> getViewClass()
  {
    return LabelView.class;
  }
}

