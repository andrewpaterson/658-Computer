package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;
import net.logicim.ui.common.defaults.DefaultFamily;

public class NorGateViewFactory
    extends ViewFactory
{
  @Override
  public NorGateView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new NorGateView(circuitEditor,
                           2,
                           position,
                           rotation,
                           "",
                           DefaultFamily.get(),
                           true);
  }

  @Override
  public Class<? extends DiscreteView<?>> getViewClass()
  {
    return NorGateView.class;
  }
}

