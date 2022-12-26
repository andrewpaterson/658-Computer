package net.logicim.ui.integratedcircuit.standard.logic.and;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.component.DiscreteView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ViewFactory;
import net.logicim.ui.common.defaults.DefaultFamily;

public class NandGateViewFactory
    extends ViewFactory
{
  @Override
  public DiscreteView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation)
  {
    return new NandGateView(circuitEditor,
                            2,
                            position,
                            rotation,
                            "",
                            DefaultFamily.get(),
                            true);
  }
}

