package net.logicim.ui.integratedcircuit.factory;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteView;

public abstract class ViewFactory
{
  public abstract DiscreteView<?> create(CircuitEditor circuitEditor, Int2D position, Rotation rotation);

  public abstract Class<? extends DiscreteView<?>> getViewClass();
}

