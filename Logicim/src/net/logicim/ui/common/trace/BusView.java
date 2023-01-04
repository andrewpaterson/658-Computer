package net.logicim.ui.common.trace;

import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;

public class BusView
    extends BaseTraceView
{
  public BusView(CircuitEditor circuitEditor, Int2D start, Int2D end)
  {
    super(circuitEditor, start, end);
//    this.trace = null;
//    circuitEditor._addTraceView(this);
  }

  @Override
  public String getName()
  {
    return "Bus";
  }

  @Override
  public String getDescription()
  {
    return "Bus (" + getStartPosition() + ") to (" + getEndPosition() + ")";
  }
}

