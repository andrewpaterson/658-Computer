package net.logicim.ui.common;

import net.common.type.Int2D;
import net.logicim.ui.common.integratedcircuit.View;

public class HoverConnectionView
    extends ConnectionView
{
  public HoverConnectionView(View view, int x, int y)
  {
    super(view, x, y);
  }

  public Int2D getGridPosition()
  {
    return gridPosition;
  }

  @Override
  public boolean isConcrete()
  {
    return false;
  }
}

