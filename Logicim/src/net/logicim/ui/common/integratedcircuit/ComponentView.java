package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.List;

public abstract class ComponentView
{
  protected List<PortView> ports;

  public PortView getPortView(Port port)
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

  public Int2D getGridPosition(ConnectionView connectionView)
  {
    for (PortView portView : ports)
    {
      ConnectionView portViewConnections = portView.getConnection();
      if (portViewConnections == connectionView)
      {
        return portView.getGridPosition();
      }
    }
    return null;
  }

  public abstract void paintSelected(Graphics2D graphics, Viewport viewport);

  protected void paintSelectionRectangle(Graphics2D graphics, Viewport viewport, int x, int y, Color viewHover)
  {
    float zoom = viewport.getZoom();
    float radius = zoom * 3;
    int left = (int) (x - radius);
    int top = (int) (y - radius);
    int width = (int) (radius * 2);
    int height = (int) (radius * 2);
    graphics.setColor(viewHover);
    graphics.fillRect(left, top, width, height);
    graphics.setColor(Color.BLACK);
    graphics.drawRect(left, top, width, height);
  }

  public abstract String getName();

  public abstract String getDescription();

  public abstract Int2D getGridPosition();

  public abstract void enable(Simulation simulation);

  public abstract void disable();

  public abstract void setPosition(int x, int y);
}
