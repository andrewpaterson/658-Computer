package net.logicim.ui.trace;

import net.logicim.common.type.Int2D;

public class JunctionView
{
  protected Int2D position;

  public JunctionView(Int2D position)
  {
    this.position = position;
  }

  public Int2D getPosition()
  {
    return position;
  }
}

