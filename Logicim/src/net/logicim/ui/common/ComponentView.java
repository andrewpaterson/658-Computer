package net.logicim.ui.common;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.port.BasePort;

import java.awt.*;
import java.util.List;

public abstract class ComponentView
{
  protected List<PortView> ports;

  public PortView getPortView(BasePort port)
  {
    for (PortView portView : ports)
    {
      if (portView.getPort() == port)
      {
        return portView;
      }
    }
    return null;
  }

  protected void paintPorts(Graphics2D graphics, Viewport viewport, long time)
  {
    for (PortView portView : ports)
    {
      portView.paint(graphics, viewport, time);
    }
  }

  public PortView getPortInGrid(int x, int y)
  {
    for (PortView port : ports)
    {
      if (port.getGridPosition().equals(x, y))
      {
        return port;
      }
    }
    return null;
  }

  public PortView getPortInGrid(Int2D position)
  {
    return getPortInGrid(position.x, position.y);
  }

  public abstract ConnectionView getConnectionsInGrid(int x, int y);

  public abstract ConnectionView getConnectionsInGrid(Int2D p);

  public abstract Int2D getGridPosition(ConnectionView connectionView);

  public abstract String getName();

  public abstract String getDescription();
}

