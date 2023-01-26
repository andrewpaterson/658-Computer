package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.port.MultiPortData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ComponentView<PROPERTIES extends ComponentProperties>
    extends StaticView<PROPERTIES>
    implements ShapeHolder
{
  protected List<PortView> ports;

  public ComponentView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, PROPERTIES properties)
  {
    super(circuitEditor, position, rotation, properties);
    this.properties = properties;

    this.ports = new ArrayList<>();
  }

  protected void finaliseView()
  {
    finalised = true;

    updateBoundingBox();
  }

  @Override
  protected void updateBoundingBox()
  {
    if (boundingBox.isNull())
    {
      updateBoundingBoxFromPorts(boundingBox);
      updateSelectionBox();
    }
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

  @Override
  public String getName()
  {
    return properties.name;
  }

  public PROPERTIES getProperties()
  {
    return properties;
  }

  public void setProperties(PROPERTIES properties)
  {
    this.properties = properties;
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

  public PortView getPort(ConnectionView connectionView)
  {
    for (PortView portView : ports)
    {
      if (portView.getConnection() == connectionView)
      {
        return portView;
      }
    }
    return null;
  }

  public Int2D getConnectionGridPosition(ConnectionView connectionView)
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

  protected void updateBoundingBoxFromPorts(BoundingBox boundingBox)
  {
    for (PortView port : ports)
    {
      port.updateBoundingBox(boundingBox);
    }
  }

  @Override
  public void enable(Simulation simulation)
  {
    getComponent().enable(simulation);
  }

  @Override
  public void disable()
  {
    getComponent().disable();
  }

  @Override
  public String getDescription()
  {
    return getComponent().getType() + " " + getName() + " (" + getPosition() + ")";
  }

  public boolean isEnabled()
  {
    return getComponent().isEnabled();
  }

  protected abstract void createPortViews();

  public abstract Component getComponent();

  public void disconnect(Simulation simulation, ConnectionView connection)
  {
    for (PortView portView : ports)
    {
      if (portView.getConnection() == connection)
      {
        portView.disconnect(simulation);
      }
    }
  }

  public abstract ComponentData save(boolean selected);

  public abstract void simulationStarted(Simulation simulation);
}

