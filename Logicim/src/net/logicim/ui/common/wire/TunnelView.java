package net.logicim.ui.common.wire;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public class TunnelView
    extends WireView
{
  protected ConnectionView connection;
  protected String name;
  protected Int2D position;

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    if (position.equals(x, y))
    {
      return connection;
    }
    return null;
  }

  @Override
  public Int2D getConnectionGridPosition(ConnectionView connectionView)
  {
    if (this.connection == connectionView)
    {
      return position;
    }
    return null;
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
  }

  @Override
  public void paintSelected(Graphics2D graphics, Viewport viewport)
  {
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getDescription()
  {
    return "Tunnel [" + name + "] (" + position + ")";
  }

  @Override
  public Int2D getPosition()
  {
    return position;
  }

  @Override
  public void setPosition(int x, int y)
  {
    position.set(x, y);
  }
}

