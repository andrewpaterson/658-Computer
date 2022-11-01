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
}

