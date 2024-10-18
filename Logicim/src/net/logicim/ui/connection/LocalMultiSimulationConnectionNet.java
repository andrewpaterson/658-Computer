package net.logicim.ui.connection;

import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.port.PortViewFinder;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.util.*;

public class LocalMultiSimulationConnectionNet
{
  protected List<LocalConnectionNet> localConnectionNets;

  protected Map<CircuitInstanceViewPath, List<ComponentConnection<ComponentView<?>>>> connectedComponents;
  protected Map<CircuitInstanceViewPath, List<WireConnection>> connectedWires;
  protected List<ComponentConnection<PinView>> pinViews;

  protected List<ComponentViewPortNames> componentViewPortNamesList;  //These are the editor 'traces'.

  public LocalMultiSimulationConnectionNet()
  {
    this.localConnectionNets = new ArrayList<>();
    this.connectedComponents = new LinkedHashMap<>();
    this.connectedWires = new LinkedHashMap<>();
    this.pinViews = new ArrayList<>();
  }

  public void add(LocalConnectionNet localConnectionNet)
  {
    localConnectionNets.add(localConnectionNet);
  }

  public void process()
  {
    int minimumPorts = calculateMinimumPorts(localConnectionNets);

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

  private int calculateMinimumPorts(List<LocalConnectionNet> localConnectionNets)
  {
    int minimumPorts = Integer.MAX_VALUE;
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      Set<ConnectionView> connectionViews = localConnectionNet.getConnectionViews();
      int currentMinimumPorts = calculateMinimumPorts(connectionViews);
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

  protected int calculateMinimumPorts(Collection<ConnectionView> connections)
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

  protected void findConnections(List<LocalConnectionNet> localConnectionNets)
  {
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      Set<ConnectionView> connections = localConnectionNet.getConnectionViews();
      CircuitInstanceViewPath path = localConnectionNet.getPath();
      for (ConnectionView connectionView : connections)
      {
        List<View> localConnected = connectionView.getConnectedComponents();
        for (View connectedView : localConnected)
        {
          if (connectedView instanceof ComponentView)
          {
            ComponentView<?> componentView = (ComponentView<?>) connectedView;
            if (connectedView instanceof PinView)
            {
              pinViews.add(new ComponentConnection<>(path, (PinView) connectedView, connectionView));
            }

            addConnectedComponent(path, connectionView, componentView);
          }
          else if (connectedView instanceof WireView)
          {
            addConnectedWire(path, connectionView, (WireView) connectedView);
          }
        }
      }
    }
  }

  private void addConnectedWire(CircuitInstanceViewPath path,
                                ConnectionView connectionView,
                                WireView connectedView)
  {
    List<WireConnection> wireConnections = connectedWires.get(path);
    if (wireConnections == null)
    {
      wireConnections = new ArrayList<>();
      connectedWires.put(path, wireConnections);
    }
    wireConnections.add(new WireConnection(path, connectedView, connectionView));
  }

  private void addConnectedComponent(CircuitInstanceViewPath path,
                                     ConnectionView connectionView,
                                     ComponentView<?> componentView)
  {
    List<ComponentConnection<ComponentView<?>>> componentConnections = connectedComponents.get(path);
    if (componentConnections == null)
    {
      componentConnections = new ArrayList<>();
      connectedComponents.put(path, componentConnections);
    }
    componentConnections.add(new ComponentConnection<>(path, componentView, connectionView));
  }

  protected void addPortToComponentViewPortNames(List<ComponentViewPortNames> componentViewPortNamesList)
  {
    for (Map.Entry<CircuitInstanceViewPath, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      addPortToComponentViewPortNames(componentViewPortNamesList, entry.getValue());
    }
  }

  private void addPortToComponentViewPortNames(List<ComponentViewPortNames> componentViewPortNamesList, List<ComponentConnection<ComponentView<?>>> componentConnections)
  {
    for (ComponentConnection<ComponentView<?>> connectedComponent : componentConnections)
    {
      ComponentView<?> componentView = connectedComponent.getComponent();
      ConnectionView connectionView = connectedComponent.getConnection();
      CircuitInstanceViewPath path = connectedComponent.getPath();

      PortView portView = componentView.getPortView(connectionView);
      List<String> portNames = portView.getPortNames();

      addPortToComponentViewPortNames(componentViewPortNamesList,
                                      componentView,
                                      portNames,
                                      path);
    }
  }

  private void addPortToComponentViewPortNames(List<ComponentViewPortNames> componentViewPortNamesList,
                                               ComponentView<?> componentView,
                                               List<String> portNames,
                                               CircuitInstanceViewPath path)
  {
    for (int i = 0; i < componentViewPortNamesList.size(); i++)
    {
      String portName = portNames.get(i);
      ComponentViewPortNames componentViewPortNames = componentViewPortNamesList.get(i);
      componentViewPortNames.addPort(componentView, portName, path);
    }
  }

  public Map<CircuitInstanceViewPath, List<WireConnection>> getConnectedWires()
  {
    return connectedWires;
  }

  public List<ComponentViewPortNames> getComponentViewPortNamesList()
  {
    return componentViewPortNamesList;
  }

  public Map<CircuitInstanceViewPath, Set<ConnectionView>> getConnectionViews()
  {
    LinkedHashMap<CircuitInstanceViewPath, Set<ConnectionView>> result = new LinkedHashMap<>();

    for (Map.Entry<CircuitInstanceViewPath, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      CircuitInstanceViewPath path = entry.getKey();
      Set<ConnectionView> connectionViews = new LinkedHashSet<>();
      result.put(path, connectionViews);

      List<ComponentConnection<ComponentView<?>>> componentConnections = entry.getValue();
      for (ComponentConnection<ComponentView<?>> componentConnection : componentConnections)
      {
        ConnectionView connectionView = componentConnection.connection;
        connectionViews.add(connectionView);
      }
    }

    for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectedWires.entrySet())
    {
      CircuitInstanceViewPath path = entry.getKey();
      Set<ConnectionView> connectionViews = result.get(path);
      if (connectionViews == null)
      {
        connectionViews = new LinkedHashSet<>();
        result.put(path, connectionViews);
      }

      List<WireConnection> wireConnections = entry.getValue();
      for (WireConnection wireConnection : wireConnections)
      {
        connectionViews.addAll(wireConnection.getWireView().getConnectionViews());
      }
    }

    return result;
  }

  public String toString()
  {
    StringBuilder builder = new StringBuilder();

    for (Map.Entry<CircuitInstanceViewPath, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      List<ComponentConnection<ComponentView<?>>> componentConnections = entry.getValue();
      for (ComponentConnection<ComponentView<?>> componentConnection : componentConnections)
      {
        ConnectionView connectionView = componentConnection.connection;
        ComponentView<?> componentView = componentConnection.component;
        builder.append(componentView.getType() + " (" + connectionView.getGridPosition() + ")@" + System.identityHashCode(connectionView) + "\n");
      }
    }

    return builder.toString();
  }
}

