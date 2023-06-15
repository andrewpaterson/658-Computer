package net.logicim.ui.common.integratedcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.common.util.StringUtil;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.InstanceCircuitSimulation;
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

  public List<ConnectionView> getOrCreateConnectionViews(SubcircuitView subcircuitView)
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

  public PortView getPortView(InstanceCircuitSimulation circuit, Port port)
  {
    for (PortView portView : portViews)
    {
      if (portView.containsPort(circuit, port))
      {
        return portView;
      }
    }
    return null;
  }

  protected void paintPorts(Graphics2D graphics, Viewport viewport, InstanceCircuitSimulation circuit)
  {
    for (PortView portView : portViews)
    {
      portView.paint(graphics, viewport, circuit);
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
  public List<ConnectionView> getConnectionViews()
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
    String name = getName();
    if (!StringUtil.isEmptyOrNull(name))
    {
      name = name + " ";
    }
    else
    {
      name = "";
    }
    String componentType = getComponentType();
    if (StringUtil.isEmptyOrNull(componentType))
    {
      componentType = componentType + " ";
    }
    else
    {
      componentType = "";
    }
    return componentType + name + "(" + getPosition() + ")";
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

  protected void validatePorts(InstanceCircuitSimulation circuit, List<Port> ports, List<PortView> portViews)
  {
    if ((ports.size() > 0) && (portViews.size() == 0))
    {
      throw new SimulatorException("Ports not configured on view.  Call new PortView(Port) for each Port on view [%s].", getDescription());
    }

    validateAtLeastOnePort(portViews);
    validateNoMissingPorts(circuit, ports);
    validateNoDuplicatePorts(ports);
  }

  protected void validateNoMissingPorts(InstanceCircuitSimulation circuit, List<Port> ports)
  {
    List<Port> missing = new ArrayList<>();
    for (Port port : ports)
    {
      if (port.isLogicPort())
      {
        PortView portView = getPortView(circuit, port);
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

  protected void validateCanCreateComponent(InstanceCircuitSimulation circuit)
  {
    if (circuit == null)
    {
      throw new SimulatorException("Cannot create %s component with [null] simulation.", getClass().getSimpleName());
    }

    Component component = getComponent(circuit);
    if (component != null)
    {
      throw new SimulatorException("[%s] component has already been created.", component.getDescription());
    }
  }

  protected void validateComponent(InstanceCircuitSimulation circuit)
  {
    if (circuit != null)
    {
      Component component = getComponent(circuit);
      if (component == null)
      {
        throw new SimulatorException("Component configured in simulation [%s] on [%s].  Call create().",
                                     circuit.getDescription(),
                                     getClass().getSimpleName());
      }
    }
  }

  protected void validatePorts(InstanceCircuitSimulation circuit)
  {
    Component component = getComponent(circuit);
    if (component != null)
    {
      validatePorts(circuit, component.getPorts(), portViews);
    }
  }

  protected void postCreateComponent(InstanceCircuitSimulation circuit, Component component)
  {
    addPortsToPortViews(circuit, component);
    validateComponent(circuit);
    validatePorts(circuit);
    component.reset(circuit);
  }

  private void addPortsToPortViews(InstanceCircuitSimulation circuit, Component component)
  {
    List<PortView> portViews = getPortViews();
    for (PortView portView : portViews)
    {
      List<Port> ports = new ArrayList<>();
      List<String> portNames = portView.getPortNames();
      for (String portName : portNames)
      {
        Port port = component.getPort(portName);
        if (port != null)
        {
          ports.add(port);
        }
        else
        {
          throw new SimulatorException("Cannot find port named [%s].", portName);
        }
      }
      portView.addPorts(circuit, ports);
    }
  }

  public void destroyComponent(InstanceCircuitSimulation circuit)
  {
    for (PortView portView : portViews)
    {
      portView.removePorts(circuit);
    }
    removeComponent(circuit);
  }

  @Override
  public void simulationStarted(InstanceCircuitSimulation circuit)
  {
    if (circuit == null)
    {
      throw new SimulatorException("Cannot start a simulation with a [null] simulation.");
    }

    Component integratedCircuit = getComponent(circuit);
    integratedCircuit.simulationStarted(circuit.getSimulation());
  }

  protected abstract void createPortViews();

  public abstract Component getComponent(InstanceCircuitSimulation circuit);

  protected abstract void removeComponent(InstanceCircuitSimulation circuit);

  public abstract String getComponentType();

  public abstract ComponentData<?> save(boolean selected);
}

