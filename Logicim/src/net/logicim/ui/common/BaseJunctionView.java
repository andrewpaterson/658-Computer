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
  public Int2D getGridPosition()
  {
    return position;
  }

  @Override
  public boolean equals(int x, int y)
  {
    return position.equals(x, y);
  }

  public void set(Int2D position)
  {
    this.position.set(position);
  }

  public void set(int x, int y)
  {
    this.position.set(x, y);
  }
}

