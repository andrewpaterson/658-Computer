package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
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

  public List<ConnectionView> createConnections(CircuitEditor circuitEditor)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    List<PortView> portViews = getPorts();
    for (PortView portView : portViews)
    {
      Int2D portPosition = portView.getGridPosition();
      ConnectionView connectionView = circuitEditor.getOrAddConnection(portPosition, this);
      portView.setConnection(connectionView);
      connectionViews.add(connectionView);
    }
    return connectionViews;
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

  public void addPortView(PortView portView)
  {
    ports.add(portView);
  }

  public List<PortView> getPorts()
  {
    return ports;
  }

  @Override
  public List<ConnectionView> getConnections()
  {
    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    for (PortView port : ports)
    {
      ConnectionView connectionView = port.getConnection();
      if (connectionView != null)
      {
        connectionViews.add(connectionView);
      }
    }
    return connectionViews;
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
        return;
      }
    }

    throw new SimulatorException("Could not disconnect connection from %s.", toIdentifierString());
  }

  @Override
  public void disconnect(Simulation simulation)
  {
    for (PortView portView : ports)
    {
      portView.disconnect(simulation);
      return;
    }
  }

  public abstract ComponentData<?> save(boolean selected);

  public abstract void simulationStarted(Simulation simulation);
}

