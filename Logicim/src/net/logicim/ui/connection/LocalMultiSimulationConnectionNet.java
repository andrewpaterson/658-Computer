package net.logicim.ui.connection;

import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.port.PortViewFinder;
import net.logicim.ui.common.wire.WireView;

import java.util.*;

public class LocalMultiSimulationConnectionNet
{
  protected Set<LocalConnectionNet> localConnectionNets;

  protected Map<ViewPath, List<ComponentConnection<ComponentView<?>>>> connectedComponents;
  protected Map<ViewPath, List<WireViewPathConnection>> connectedWires;

  protected List<ComponentViewPortNames> componentViewPortNamesList;  //These are the editor 'traces'.

  public LocalMultiSimulationConnectionNet()
  {
    this.localConnectionNets = new LinkedHashSet<>();
    this.connectedComponents = new LinkedHashMap<>();
    this.connectedWires = new LinkedHashMap<>();
  }

  public void process()
  {
    int minimumPorts = calculateLocalConnectionNetMinimumPorts(localConnectionNets);

    if (isValid(minimumPorts))
    {
      componentViewPortNamesList = createComponentViewPortNamesList(minimumPorts);

      findConnections(localConnectionNets);
      addPortToComponentViewPortNames(componentViewPortNamesList);
    }
    else
    {
      componentViewPortNamesList = new ArrayList<>();

      findConnections(localConnectionNets);
    }
  }

  public void add(LocalConnectionNet localConnectionNet)
  {
    localConnectionNets.add(localConnectionNet);
  }

  private boolean isValid(int minimumPorts)
  {
    return minimumPorts != Integer.MAX_VALUE;
  }

  protected List<ComponentViewPortNames> createComponentViewPortNamesList(int minimumPorts)
  {
    List<ComponentViewPortNames> componentViewPortNames = new ArrayList<>(minimumPorts);
    for (int i = 0; i < minimumPorts; i++)
    {
      componentViewPortNames.add(new ComponentViewPortNames(this));
    }
    return componentViewPortNames;
  }

  private int calculateLocalConnectionNetMinimumPorts(Collection<LocalConnectionNet> localConnectionNets)
  {
    int minimumPorts = Integer.MAX_VALUE;
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      List<ConnectionView> connectionViews = localConnectionNet.getConnectionViews();
      int currentMinimumPorts = calculateConnectionViewMinimumPorts(connectionViews);
      if (!isValid(currentMinimumPorts))
      {
        minimumPorts = Integer.MAX_VALUE;
        break;
      }
      else
      {
        if (currentMinimumPorts < minimumPorts)
        {
          minimumPorts = currentMinimumPorts;
        }
      }
    }
    return minimumPorts;
  }

  protected int calculateConnectionViewMinimumPorts(Collection<ConnectionView> connections)
  {
    int minimumPorts = Integer.MAX_VALUE;
    Set<PortView> portViews = PortViewFinder.findPortViews(connections);
    for (PortView portView : portViews)
    {
      if (portView.numberOfPorts() < minimumPorts)
      {
        minimumPorts = portView.numberOfPorts();
        if (minimumPorts == 1)
        {
          break;
        }
      }
    }
    return minimumPorts;
  }

  protected void findConnections(Collection<LocalConnectionNet> localConnectionNets)
  {
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      List<ConnectionView> connections = localConnectionNet.getConnectionViews();
      ViewPath viewPath = localConnectionNet.getViewPath();
      for (ConnectionView connectionView : connections)
      {
        List<View> localConnected = connectionView.getConnectedComponents();
        for (View connectedView : localConnected)
        {
          if (connectedView instanceof ComponentView)
          {
            addConnectedComponent(viewPath,
                                  connectionView,
                                  (ComponentView<?>) connectedView);
          }
          else if (connectedView instanceof WireView)
          {
            addConnectedWire(viewPath,
                             connectionView,
                             (WireView) connectedView);
          }
        }
      }
    }
  }

  private WireViewPathConnection addConnectedWire(ViewPath viewPath,
                                                  ConnectionView connectionView,
                                                  WireView connectedView)
  {
    List<WireViewPathConnection> wireViewPathConnections = connectedWires.get(viewPath);
    if (wireViewPathConnections == null)
    {
      wireViewPathConnections = new ArrayList<>();
      connectedWires.put(viewPath, wireViewPathConnections);
    }
    WireViewPathConnection wireViewPathConnection = new WireViewPathConnection(viewPath,
                                                                               connectedView,
                                                                               connectionView);
    wireViewPathConnections.add(wireViewPathConnection);
    return wireViewPathConnection;
  }

  private ComponentConnection<ComponentView<?>> addConnectedComponent(ViewPath viewPath,
                                                                      ConnectionView connectionView,
                                                                      ComponentView<?> componentView)
  {
    List<ComponentConnection<ComponentView<?>>> componentConnections = connectedComponents.get(viewPath);
    if (componentConnections == null)
    {
      componentConnections = new ArrayList<>();
      connectedComponents.put(viewPath, componentConnections);
    }
    ComponentConnection<ComponentView<?>> componentConnection = new ComponentConnection<>(viewPath, componentView, connectionView);
    componentConnections.add(componentConnection);
    return componentConnection;
  }

  protected void addPortToComponentViewPortNames(List<ComponentViewPortNames> componentViewPortNamesList)
  {
    for (Map.Entry<ViewPath, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      List<ComponentConnection<ComponentView<?>>> componentConnections = entry.getValue();
      addPortToComponentViewPortNames(componentViewPortNamesList, componentConnections);
    }
  }

  private void addPortToComponentViewPortNames(List<ComponentViewPortNames> componentViewPortNamesList,
                                               List<ComponentConnection<ComponentView<?>>> componentConnections)
  {
    for (ComponentConnection<ComponentView<?>> connectedComponent : componentConnections)
    {
      ComponentView<?> componentView = connectedComponent.getComponentView();
      ConnectionView connectionView = connectedComponent.getConnectionView();
      ViewPath viewPath = connectedComponent.getViewPath();

      PortView portView = componentView.getPortView(connectionView);
      List<String> portNames = portView.getPortNames();

      addPortToComponentViewPortNames(componentViewPortNamesList,
                                      componentView,
                                      portNames,
                                      viewPath);
    }
  }

  private void addPortToComponentViewPortNames(List<ComponentViewPortNames> componentViewPortNamesList,
                                               ComponentView<?> componentView,
                                               List<String> portNames,
                                               ViewPath viewPath)
  {
    for (int i = 0; i < componentViewPortNamesList.size(); i++)
    {
      String portName = portNames.get(i);
      ComponentViewPortNames componentViewPortNames = componentViewPortNamesList.get(i);
      componentViewPortNames.addPort(componentView, portName, viewPath);
    }
  }

  public Map<ViewPath, List<WireViewPathConnection>> getConnectedWires()
  {
    return connectedWires;
  }

  public List<ComponentViewPortNames> getComponentViewPortNamesList()
  {
    return componentViewPortNamesList;
  }

  public Map<ViewPath, Set<ConnectionView>> getConnectionViews()
  {
    LinkedHashMap<ViewPath, Set<ConnectionView>> result = new LinkedHashMap<>();

    for (Map.Entry<ViewPath, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      ViewPath viewPath = entry.getKey();
      Set<ConnectionView> connectionViews = new LinkedHashSet<>();
      result.put(viewPath, connectionViews);

      List<ComponentConnection<ComponentView<?>>> componentConnections = entry.getValue();
      for (ComponentConnection<ComponentView<?>> componentConnection : componentConnections)
      {
        ConnectionView connectionView = componentConnection.connectionView;
        connectionViews.add(connectionView);
      }
    }

    for (Map.Entry<ViewPath, List<WireViewPathConnection>> entry : connectedWires.entrySet())
    {
      ViewPath viewPath = entry.getKey();
      Set<ConnectionView> connectionViews = result.get(viewPath);
      if (connectionViews == null)
      {
        connectionViews = new LinkedHashSet<>();
        result.put(viewPath, connectionViews);
      }

      List<WireViewPathConnection> wireViewPathConnections = entry.getValue();
      for (WireViewPathConnection wireViewPathConnection : wireViewPathConnections)
      {
        connectionViews.addAll(wireViewPathConnection.getWireView().getConnectionViews());
      }
    }

    return result;
  }

  public String toString()
  {
    StringBuilder builder = new StringBuilder();

    for (Map.Entry<ViewPath, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      List<ComponentConnection<ComponentView<?>>> componentConnections = entry.getValue();
      for (ComponentConnection<ComponentView<?>> componentConnection : componentConnections)
      {
        ConnectionView connectionView = componentConnection.connectionView;
        ComponentView<?> componentView = componentConnection.componentView;
        builder.append(componentView.getType() + " (" + connectionView.getGridPosition() + ")@" + System.identityHashCode(connectionView) + "\n");
      }
    }

    return builder.toString();
  }

  public Set<ComponentViewPortNames> getLocalWires(CircuitSimulation circuitSimulation)
  {
    LinkedHashSet<ComponentViewPortNames> result = new LinkedHashSet<>();
    for (ComponentViewPortNames componentViewPortNames : componentViewPortNamesList)
    {
      for (ComponentViewPortName componentViewPortName : componentViewPortNames.getConnectedPortIndices())
      {
        if (componentViewPortName.getViewPath().containsCircuitSimulation(circuitSimulation))
        {
          result.add(componentViewPortNames);
        }
      }
    }
    return result;
  }
}

