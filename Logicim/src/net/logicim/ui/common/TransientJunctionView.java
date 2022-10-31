package net.logicim.ui.common;

import net.logicim.common.type.Int2D;

public class TransientJunctionView
    extends BaseJunctionView
{
  public TransientJunctionView()
  {
    super(new Int2D());
  }

  @Override
  public void invalidateCache()
  {
  }

  @Override
  public void getGridPosition(Int2D destination)
  {
    destination.set(position);
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

