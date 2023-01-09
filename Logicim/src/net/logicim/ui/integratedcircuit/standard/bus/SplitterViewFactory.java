package net.logicim.ui.integratedcircuit.standard.bus;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;

public class SplitterViewFactory
    extends ViewFactory<SplitterView, SplitterProperties>
{
  @Override
  public SplitterView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new SplitterView(circuitEditor,
                            position,
                            rotation,
                            new SplitterProperties("", 2, -1, 2));
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
}

