package net.logicim.ui.common;

import net.logicim.common.type.Int2D;

public abstract class ComponentView
{
  public abstract ConnectionView getConnectionsInGrid(int x, int y);

  public abstract ConnectionView getConnectionsInGrid(Int2D p);

  public abstract Int2D getGridPosition(ConnectionView connectionView);

  public abstract boolean isEnabled();
}
