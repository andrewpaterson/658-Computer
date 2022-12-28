package net.logicim.ui.common;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.integratedcircuit.ComponentView;

public class HoverConnectionView
    extends ConnectionView
{
  protected Int2D gridPosition;

  public HoverConnectionView(ComponentView componentView, int x, int y)
  {
    super(componentView);
    this.gridPosition = new Int2D(x, y);
  }

  public ComponentView getParent()
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

