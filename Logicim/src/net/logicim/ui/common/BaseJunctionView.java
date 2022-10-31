package net.logicim.ui.common;

import net.logicim.common.type.Int2D;

public abstract class BaseJunctionView
    extends ConnectionView
{
  protected Int2D position;

  public BaseJunctionView(Int2D position)
  {
    this.position = position;
  }

  @Override
  public void getGridPosition(Int2D destination)
  {
    destination.set(position);
  }

  @Override
  public boolean equals(int x, int y)
  {
    return position.equals(x, y);
  }
}

