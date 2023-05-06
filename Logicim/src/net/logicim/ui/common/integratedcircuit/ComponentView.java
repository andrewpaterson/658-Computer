package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.port.Port;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class ComponentView<PROPERTIES extends ComponentProperties>
    extends StaticView<PROPERTIES>
    implements ShapeHolder
{
  protected List<PortView> portViews;

  public ComponentView(SubcircuitView subcircuitView,
                       Int2D position,
                       Rotation rotation,
                       PROPERTIES properties)
  {
    super(subcircuitView,
          position,
          rotation,
          properties);
    this.portViews = new ArrayList<>();
  }

  protected void finaliseView()
  {
    finalised = true;

    updateBoundingBoxes();
  }

  @Override
  protected void updateBoundingBoxes()
  {
    if (boundingBox.isNull())
    {
      updateBoundingBoxFromPorts(boundingBox);
      updateBoundingBoxAndSelectionBox();
    }
  }

  protected void invalidateCache()
  {
    super.invalidateCache();

    for (PortView port : portViews)
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

  public List<ConnectionView> createConnectionViews(SubcircuitView subcircuitView)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    List<PortView> portViews = getPortViews();
    for (PortView portView : portViews)
    {
      Int2D portPosition = portView.getGridPosition();
      ConnectionView connectionView = subcircuitView.getOrAddConnectionView(portPosition, this);
      portView.setConnection(connectionView);
      connectionViews.add(connectionView);
    }
    return connectionViews;
  }

  public PortView getPortView(CircuitSimulation simulation, Port port)
  {
    for (PortView portView : portViews)
    {
      if (portView.containsPort(simulation, port))
      {
        return portView;
      }
    }
    return null;
  }

  protected void paintPorts(Graphics2D graphics, Viewport viewport, CircuitSimulation simulation)
  {
    for (PortView portView : portViews)
    {
      portView.paint(graphics, viewport, simulation);
    }
  }

  public void addPortView(PortView portView)
  {
    portViews.add(portView);
  }

  public List<PortView> getPortViews()
  {
    return portViews;
  }

  @Override
  public List<ConnectionView> getConnections()
  {
    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    for (PortView port : portViews)
    {
      ConnectionView connectionView = port.getConnection();
      if (connectionView != null)
      {
        connectionViews.add(connectionView);
      }
    }
    return connectionViews;
  }

  protected List<SimulationMultiPortData> savePorts()
  {
    List<SimulationMultiPortData> portDatas = new ArrayList<>(portViews.size());
    for (PortView port : portViews)
    {
      SimulationMultiPortData portData = port.save();
      portDatas.add(portData);
    }
    return portDatas;
  }

  public PortView getPortView(int index)
  {
    return portViews.get(index);
  }

  public PortView getPort(ConnectionView connectionView)
  {
    for (PortView portView : portViews)
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
    for (PortView port : portViews)
    {
      port.updateBoundingBox(boundingBox);
    }
  }

  @Override
  public String getDescription()
  {
    return getComponentType() + " " + getName() + " (" + getPosition() + ")";
  }

  protected void validateAtLeastOnePort(List<PortView> portViews)
  {
    for (PortView portView : portViews)
    {
      List<String> portNames = portView.getPortNames();
      if (portNames.size() < 1)
      {
        throw new SimulatorException("PortView [%s] must have at lease one port on view [%s].", portView.getText(), getDescription());
      }
    }
  }

  @Override
  public void disconnect()
  {
    for (PortView portView : portViews)
    {
      portView.disconnect();
      return;
    }
  }

  protected void validatePorts(CircuitSimulation simulation, List<Port> ports, List<PortView> portViews)
  {
    if ((ports.size() > 0) && (portViews.size() == 0))
    {
      throw new SimulatorException("Ports not configured on view.  Call new PortView(Port) for each Port on view [%s].", getDescription());
    }

    validateAtLeastOnePort(portViews);
    validateNoMissingPorts(simulation, ports);
    validateNoDuplicatePorts(ports);
  }

  protected void validateNoMissingPorts(CircuitSimulation simulation, List<Port> ports)
  {
    List<Port> missing = new ArrayList<>();
    for (Port port : ports)
    {
      if (port.isLogicPort())
      {
        PortView portView = getPortView(simulation, port);
        if (portView == null)
        {
          missing.add(port);
        }
      }
    }

    if (missing.size() > 0)
    {
      List<String> missingNames = new ArrayList<>();
      for (Port port : missing)
      {
        missingNames.add(port.getName());
      }
      throw new SimulatorException("Ports [%s] not configured on view.  Call new PortView(Port) for each Port on view [%s].", StringUtil.commaSeparateList(missingNames), getDescription());
    }
  }

  protected void validateNoDuplicatePorts(List<Port> ports)
  {
    Set<String> portNames = new HashSet<>();
    Set<String> duplicatePortNames = new LinkedHashSet<>();
    for (Port port : ports)
    {
      String portName = port.getName();
      if (portNames.contains(portName))
      {
        duplicatePortNames.add(portName);
      }
      portNames.add(portName);
    }

    if (duplicatePortNames.size() > 0)
    {
      throw new SimulatorException("Duplicate Ports [%s] on view [%s].", StringUtil.commaSeparateList(new ArrayList<>(duplicatePortNames)), getDescription());
    }
  }

  protected abstract void createPortViews();

  public abstract Component getComponent(CircuitSimulation simulation);

  public abstract String getComponentType();

  public abstract ComponentData<?> save(boolean selected);

  public abstract void simulationStarted(CircuitSimulation simulation);

  public abstract Component createComponent(CircuitSimulation simulation);
}

