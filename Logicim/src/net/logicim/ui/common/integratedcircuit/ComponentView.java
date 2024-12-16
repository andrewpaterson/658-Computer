package net.logicim.ui.common.integratedcircuit;

import net.common.SimulatorException;
import net.common.type.Int2D;
import net.common.util.StringUtil;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.CircuitElement;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.Ports;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathComponentSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.component.common.InstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.awt.*;
import java.util.List;
import java.util.*;

public abstract class ComponentView<PROPERTIES extends ComponentProperties>
    extends StaticView<PROPERTIES>
    implements ShapeHolder,
               InstanceView
{
  protected List<PortView> portViews;

  public ComponentView(SubcircuitView containingSubcircuitView,
                       Int2D position,
                       Rotation rotation,
                       PROPERTIES properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);
    this.portViews = new ArrayList<>();
  }

  protected void finaliseView()
  {
    rotatePortsByRelativeRight();

    super.finaliseView();
  }

  @Override
  public void validate()
  {
    if (!isEnabled())
    {
      Set<? extends SubcircuitSimulation> simulations = getComponentSubcircuitSimulations();
      if (!simulations.isEmpty())
      {
        throw new SimulatorException("%s [%s] is disabled but has [%s] Simulations.", getClass().getSimpleName(), getDescription(), simulations.size());
      }
    }
  }

  private void rotatePortsByRelativeRight()
  {
    for (PortView portView : portViews)
    {
      if (relativeRightRotations != 0)
      {
        Int2D relativePosition = portView.getRelativePosition();
        Rotation rotation = Rotation.North.rotateRight(relativeRightRotations);
        rotation.transform(relativePosition);
      }
    }
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

  public List<ConnectionView> getOrCreateConnectionViews()
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    List<PortView> portViews = getPortViews();
    for (PortView portView : portViews)
    {
      Int2D portPosition = portView.getGridPosition();
      ConnectionView connectionView = containingSubcircuitView.getOrAddConnectionView(portPosition, this);
      portView.setConnection(connectionView);
      connectionViews.add(connectionView);
    }
    return connectionViews;
  }

  public PortView getPortView(ViewPath path,
                              CircuitSimulation circuitSimulation,
                              Port port)
  {
    for (PortView portView : portViews)
    {
      if (portView.containsPort(path,
                                circuitSimulation,
                                port))
      {
        return portView;
      }
    }
    return null;
  }

  protected void paintPorts(Graphics2D graphics,
                            Viewport viewport,
                            ViewPath path,
                            CircuitSimulation circuitSimulation)
  {
    for (PortView portView : portViews)
    {
      portView.paint(graphics,
                     viewport,
                     path,
                     circuitSimulation);
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
    List<ConnectionView> connectionViews = new ArrayList<>();
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

  public PortView getPortView(ConnectionView connectionView)
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

  protected void validatePorts(ViewPath path,
                               CircuitSimulation circuitSimulation,
                               List<Port> ports,
                               List<PortView> portViews)
  {
    if ((ports.size() > 0) && (portViews.size() == 0))
    {
      throw new SimulatorException("Ports not configured on view.  Call new PortView(Port) for each Port on view [%s].", getDescription());
    }

    validateAtLeastOnePort(portViews);
    validateNoMissingPorts(path,
                           circuitSimulation,
                           ports);
    validateNoDuplicatePorts(ports);
  }

  protected void validateNoMissingPorts(ViewPath path,
                                        CircuitSimulation circuitSimulation,
                                        List<Port> ports)
  {
    List<Port> missing = new ArrayList<>();
    for (Port port : ports)
    {
      if (port.isLogicPort())
      {
        PortView portView = getPortView(path,
                                        circuitSimulation,
                                        port);
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

  protected void validateCanCreateComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    SubcircuitSimulation subcircuitSimulation = path.getSubcircuitSimulation(circuitSimulation);
    if (subcircuitSimulation == null)
    {
      throw new SimulatorException("Cannot create component with simulation [null] for %s, [%s].", getClass().getSimpleName(), getDescription());
    }

    Component component = getComponent(path, circuitSimulation);
    if (component != null)
    {
      throw new SimulatorException("[%s] component has already been created for simulation [%s] for %s [%s].", component.getDescription(), subcircuitSimulation.getDescription(), getClass().getSimpleName(), getDescription());
    }
  }

  protected void validateComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    SubcircuitSimulation subcircuitSimulation = path.getSubcircuitSimulation(circuitSimulation);
    if (subcircuitSimulation != null)
    {
      Component component = getComponent(path, circuitSimulation);
      if (component == null)
      {
        throw new SimulatorException("Component configured in simulation [%s] on [%s].  Call create().",
                                     subcircuitSimulation.getDescription(),
                                     getClass().getSimpleName());
      }
    }

    validatePorts(path, circuitSimulation);
  }

  protected void validatePorts(ViewPath path, CircuitSimulation circuitSimulation)
  {
    Component component = getComponent(path, circuitSimulation);
    if (component != null)
    {
      validatePorts(path,
                    circuitSimulation,
                    component.getPorts(),
                    portViews);
    }
  }

  protected void postCreateComponent(ViewPath path,
                                     CircuitSimulation circuitSimulation,
                                     Component component)
  {
    addPortsToPortViews(path, circuitSimulation, component);
    validateComponent(path, circuitSimulation);
  }

  private void addPortsToPortViews(ViewPath path, CircuitSimulation circuitSimulation, Component component)
  {
    SubcircuitSimulation containingSubcircuitSimulation = path.getSubcircuitSimulation(circuitSimulation);
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
      portView.addPorts(path, circuitSimulation, new Ports(ports, containingSubcircuitSimulation));
    }
  }

  @Override
  public void disconnectViewAndDestroyComponents()
  {
    for (PortView portView : portViews)
    {
      portView.disconnectViewAndDestroyComponents();
    }
    destroyAllComponents();
  }

  protected void destroyPortViewComponents(ViewPath path, CircuitSimulation circuitSimulation)
  {
    for (PortView portView : portViews)
    {
      portView.destroyComponent(path, circuitSimulation);
    }
  }

  @Override
  public void simulationStarted(CircuitSimulation circuitSimulation)
  {
    Map<SubcircuitSimulation, ? extends CircuitElement> components = getViewPathComponentSimulation().getSubcircuitSimulationComponents(circuitSimulation);
    for (Map.Entry<SubcircuitSimulation, ? extends CircuitElement> entry : components.entrySet())
    {
      Component component = (Component) entry.getValue();
      component.reset(circuitSimulation.getSimulation());
    }
  }

  @Override
  public void simulationStarted(ViewPath path, CircuitSimulation circuitSimulation)
  {
    Component component = getComponent(path, circuitSimulation);
    component.reset(circuitSimulation.getSimulation());
  }

  public Port getPort(String portName, ViewPath path, CircuitSimulation circuitSimulation)
  {
    Component component = getComponent(path, circuitSimulation);
    if (component != null)
    {
      return component.getPort(portName);
    }
    else
    {
      return null;
    }
  }

  protected String calculatePortName(String name, int i, int bufferCount)
  {
    if (bufferCount > 1)
    {
      return name + " " + i;
    }
    else
    {
      return name;
    }
  }

  @Override
  public int compareTo(InstanceView obj)
  {
    if (obj instanceof SubcircuitEditor)
    {
      return 1;
    }
    else if (obj instanceof ComponentView)
    {
      ComponentView other = (ComponentView) obj;
      return Long.compare(id, other.id);
    }
    else
    {
      throw new SimulatorException("Don't know how to compare [%s] to [%s].", this.getClass().getSimpleName(), obj.getClass().getSimpleName());
    }
  }

  public abstract ViewPathComponentSimulation<?> getViewPathComponentSimulation();

  public abstract Component getComponent(ViewPath path, CircuitSimulation circuitSimulation);

  public abstract Component createComponent(ViewPath path, CircuitSimulation circuitSimulation);

  public abstract String getComponentType();

  public abstract Set<? extends SubcircuitSimulation> getComponentSubcircuitSimulations();

  public abstract ComponentData<?> save(boolean selected);

  protected abstract void createPortViews();
}

