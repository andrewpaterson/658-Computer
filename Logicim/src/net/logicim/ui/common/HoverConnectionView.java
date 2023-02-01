package net.logicim.ui.common;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.integratedcircuit.View;

public class HoverConnectionView
    extends ConnectionView
{
  public HoverConnectionView(View view, int x, int y)
  {
    super(view, x, y);
  }

  public View getParent()
  {
    return connectedComponents.get(0);
  }

  public Int2D getGridPosition()
  {
    return gridPosition;
  }

  public boolean isConcrete()
  {
    return false;
  }
}

