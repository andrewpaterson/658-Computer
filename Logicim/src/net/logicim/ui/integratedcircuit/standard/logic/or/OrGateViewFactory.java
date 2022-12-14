package net.logicim.ui.integratedcircuit.standard.logic.or;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.DefaultFamily;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ViewFactory;

public class OrGateViewFactory
    extends ViewFactory
{
  @Override
  public DiscreteView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new OrGateView(circuitEditor,
                          2,
                          position,
                          rotation,
                          "",
                          DefaultFamily.get());
  }
}

