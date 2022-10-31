package net.logicim.ui.common;

import net.logicim.common.type.Int2D;

public class JunctionView
    extends ConnectionView
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

