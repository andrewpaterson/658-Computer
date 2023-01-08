package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.SemiconductorData;
import net.logicim.data.port.MultiPortData;
import net.logicim.domain.common.Semiconductor;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SemiconductorView<PROPERTIES extends ComponentProperties>
    extends ComponentView<PROPERTIES>
    implements ShapeHolder
{
  protected List<PortView> ports;

  public SemiconductorView(CircuitEditor circuitEditor,
                           Int2D position,
                           Rotation rotation,
                           PROPERTIES properties)
  {
    super(circuitEditor, position, rotation, properties);

    this.ports = new ArrayList<>();
  }

  public void setPosition(int x, int y)
  {
    super.setPosition(x, y);
    invalidateCache();
  }

  @Override
  public ConnectionView getConnectionsInGrid(int x, int y)
  {
    PortView portView = getPortInGrid(x, y);
    if (portView != null)
    {
      return portView.getConnection();
    }
    else
    {
      return null;
    }
  }

  public PortView getPortView(Port port)
  {
    for (PortView portView : ports)
    {
      if (portView.containsPort(port))
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

  protected void invalidateCache()
  {
    super.invalidateCache();
    for (PortView port : ports)
    {
      port.invalidateCache();
    }
  }

  public void addPortView(PortView portView)
  {
    ports.add(portView);
  }

  public List<PortView> getPorts()
  {
    return ports;
  }

  protected List<MultiPortData> savePorts()
  {
    List<MultiPortData> portDatas = new ArrayList<>(ports.size());
    for (PortView port : ports)
    {
      MultiPortData portData = port.save();
      portDatas.add(portData);
    }
    return portDatas;
  }

  public PortView getPort(int index)
  {
    return ports.get(index);
  }

  public Int2D getConnectionPosition(ConnectionView connectionView)
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

  @Override
  protected void updateBoundingBoxFromConnections(BoundingBox boundingBox)
  {
    for (PortView port : ports)
    {
      port.updateBoundingBox(boundingBox);
    }
  }

  protected abstract void createPortViews();

  public abstract Semiconductor getDiscrete();

  public abstract SemiconductorData save(boolean selected);
}

