package net.logicim.ui.common;

import net.logicim.common.type.Int2D;

public class JunctionView
    extends BaseJunctionView
{
  protected TraceView[] traceViews;
  protected int connectedTraces;

  public JunctionView(Int2D position)
  {
    super(position);
    this.traceViews = new TraceView[4];
    this.connectedTraces = 0;
  }

  public Int2D getPosition()
  {
    return position;
  }

  @Override
  public void invalidateCache()
  {
  }

  public boolean equals(Int2D position)
  {
    return this.position.equals(position);
  }
}

