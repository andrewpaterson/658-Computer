package net.logicim.ui.common;

import net.logicim.common.type.Int2D;

public abstract class ComponentView
{
  public abstract Int2D getGridPosition(ConnectionView connectionView);
}
