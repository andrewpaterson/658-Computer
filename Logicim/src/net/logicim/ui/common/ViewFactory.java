package net.logicim.ui.common;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;

public abstract class ViewFactory
{
  public abstract DiscreteView create(CircuitEditor circuitEditor, Int2D position, Rotation rotation);
}