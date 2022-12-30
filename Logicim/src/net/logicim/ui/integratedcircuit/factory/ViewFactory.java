package net.logicim.ui.integratedcircuit.factory;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteProperties;
import net.logicim.ui.common.integratedcircuit.DiscreteView;

public abstract class ViewFactory<DISCRETE extends DiscreteView<PROPERTIES>, PROPERTIES extends DiscreteProperties>
{
  public abstract DISCRETE create(CircuitEditor circuitEditor, Int2D position, Rotation rotation);

  public abstract DISCRETE create(CircuitEditor circuitEditor, Int2D position, Rotation rotation, PROPERTIES properties);

  public abstract Class<DISCRETE> getViewClass();
}

