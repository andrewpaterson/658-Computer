package net.logicim.ui.common.integratedcircuit;

import net.common.SimulatorException;
import net.common.type.Int2D;
import net.common.util.StringUtil;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.integratedcircuit.common.ComponentData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.Component;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulations;
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

  public PortView getPortView(SubcircuitSimulation subcircuitSimulation, Port port)
  {
    for (PortView portView : portViews)
    {
      if (portView.containsPort(subcircuitSimulation, port))
      {
        return portView;
      }
    }
    return null;
  }

  protected void paintPorts(Graphics2D graphics, Viewport viewport, SubcircuitSimulation subcircuitSimulation)
  {
    for (PortView portView : portViews)
    {
      portView.paint(graphics, viewport, subcircuitSimulation);
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

  protected void validatePorts(SubcircuitSimulation subcircuitSimulation, List<Port> ports, List<PortView> portViews)
  {
    if ((ports.size() > 0) && (portViews.size() == 0))
    {
      throw new SimulatorException("Ports not configured on view.  Call new PortView(Port) for each Port on view [%s].", getDescription());
    }

    validateAtLeastOnePort(portViews);
    validateNoMissingPorts(subcircuitSimulation, ports);
    validateNoDuplicatePorts(ports);
  }

  protected void validateNoMissingPorts(SubcircuitSimulation subcircuitSimulation, List<Port> ports)
  {
    List<Port> missing = new ArrayList<>();
    for (Port port : ports)
    {
      if (port.isLogicPort())
      {
        PortView portView = getPortView(subcircuitSimulation, port);
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

  protected void validateCanCreateComponent(SubcircuitSimulation subcircuitSimulation)
  {
    if (subcircuitSimulation == null)
    {
      throw new SimulatorException("Cannot create component with simulation [null] for %s, [%s].", getClass().getSimpleName(), getDescription());
    }

    Component component = getComponent(subcircuitSimulation);
    if (component != null)
    {
      throw new SimulatorException("[%s] component has already been created for simulation [%s] for %s [%s].", component.getDescription(), subcircuitSimulation.getDescription(), getClass().getSimpleName(), getDescription());
    }
  }

  protected void validateComponent(SubcircuitSimulation subcircuitSimulation)
  {
    if (subcircuitSimulation != null)
    {
      Component component = getComponent(subcircuitSimulation);
      if (component == null)
      {
        throw new SimulatorException("Component configured in simulation [%s] on [%s].  Call create().",
                                     subcircuitSimulation.getDescription(),
                                     getClass().getSimpleName());
      }
    }

    validatePorts(subcircuitSimulation);
  }

  protected void validatePorts(SubcircuitSimulation subcircuitSimulation)
  {
    Component component = getComponent(subcircuitSimulation);
    if (component != null)
    {
      validatePorts(subcircuitSimulation, component.getPorts(), portViews);
    }
  }

  protected void postCreateComponent(SubcircuitSimulation subcircuitSimulation, Component component)
  {
    addPortsToPortViews(subcircuitSimulation, component);
    validateComponent(subcircuitSimulation);
  }

  private void addPortsToPortViews(SubcircuitSimulation subcircuitSimulation, Component component)
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
      portView.addPorts(subcircuitSimulation, ports);
    }
  }

  @Override
  public void disconnectViewAndDestroyComponents()
  {
    for (PortView portView : portViews)
    {
      portView.disconnectViewAndDestroyAllComponents();
    }
    destroyAllComponents();
  }

  protected void destroyPortViewComponents(SubcircuitSimulation subcircuitSimulation)
  {
    for (PortView portView : portViews)
    {
      portView.destroyComponent(subcircuitSimulation);
    }
  }

  @Override
  public void simulationStarted()
  {
    for (SubcircuitSimulation subcircuitSimulation : getComponentSubcircuitSimulations())
    {
      Component component = getComponent(subcircuitSimulation);
      component.reset(subcircuitSimulation.getSimulation());
    }
  }

  @Override
  public void simulationStarted(SubcircuitSimulation subcircuitSimulation)
  {
    Component component = getComponent(subcircuitSimulation);
    component.reset(subcircuitSimulation.getSimulation());
  }

  public abstract Component getComponent(SubcircuitSimulation subcircuitSimulation);

  protected void validateNoComponents()
  {
    if (getComponentSubcircuitSimulations().size() > 0)
    {
      throw new SimulatorException("Expected no simulations or components on [%s] but found [%s].", getDescription(), getComponentSubcircuitSimulations().size());
    }
  }

  public List<? extends Component> createComponents(SubcircuitSimulations simulations)
  {
    validateNoComponents();  //Is it possible that components have been created by other SubcircuitSimulations than these simulations?

    ArrayList<Component> result = new ArrayList<>();
    for (SubcircuitSimulation subcircuitSimulation : simulations.getSubcircuitSimulations())
    {
      Component component = createComponent(subcircuitSimulation);
      result.add(component);
    }
    return result;
  }

  public SubcircuitSimulation getComponentSubcircuitSimulation(CircuitSimulation circuitSimulation)
  {
    Set<SubcircuitSimulation> componentSubcircuitSimulations = getComponentSubcircuitSimulations();
    List<SubcircuitSimulation> subcircuitSimulations = new ArrayList<>();
    for (SubcircuitSimulation subcircuitSimulation : componentSubcircuitSimulations)
    {
      if (subcircuitSimulation.getCircuitSimulation() == circuitSimulation)
      {
        subcircuitSimulations.add(subcircuitSimulation);
      }
    }

    if (subcircuitSimulations.size() > 1)
    {
      throw new SimulatorException("Found more than 1 [%s] simulations for circuit [%s] on static view [%s].", subcircuitSimulations.size(), circuitSimulation.getDescription(), getDescription());
    }

    if (subcircuitSimulations.size() == 1)
    {
      return subcircuitSimulations.get(0);
    }
    else
    {
      return null;
    }
  }

  public Port getPort(SubcircuitSimulation subcircuitSimulation, String portName)
  {
    Component component = getComponent(subcircuitSimulation);
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

  public abstract String getComponentType();

  public abstract Set<SubcircuitSimulation> getComponentSubcircuitSimulations();

  public abstract ComponentData<?> save(boolean selected);

  protected abstract void createPortViews();
}

