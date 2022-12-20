package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ViewFactory;
import net.logicim.ui.common.defaults.DefaultFamily;

public class AndGateViewFactory
    extends ViewFactory
{
  @Override
  public DiscreteView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new AndGateView(circuitEditor,
                           2,
                           position,
                           rotation,
                           "",
                           DefaultFamily.get(),
                           true);
  }
}

