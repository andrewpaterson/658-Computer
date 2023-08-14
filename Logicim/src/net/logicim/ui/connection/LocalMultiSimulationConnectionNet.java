package net.logicim.ui.connection;

import net.logicim.domain.common.wire.Trace;
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

  protected Set<Trace> traces = new LinkedHashSet<>();

  protected Map<CircuitInstanceViewPath, List<ComponentConnection<ComponentView<?>>>> connectedComponents;
  protected Map<CircuitInstanceViewPath, List<WireConnection>> connectedWires;
  protected List<ComponentConnection<PinView>> pinViews;

  protected List<ComponentViewPortNames> componentViewPortNames;  //These are the editor 'traces'.

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
      componentViewPortNames = createPortConnections(minimumPorts);

      findConnections(localConnectionNets);
      addWirePortIndices();
    }
    else
    {
      componentViewPortNames = new ArrayList<>();

      findConnections(localConnectionNets);
    }
  }

  private boolean isValid(int minimumPorts)
  {
    return minimumPorts != Integer.MAX_VALUE;
  }

  protected List<ComponentViewPortNames> createPortConnections(int minimumPorts)
  {
    List<ComponentViewPortNames> componentViewPortNames = new ArrayList<>(minimumPorts);
    for (int i = 0; i < minimumPorts; i++)
    {
      componentViewPortNames.add(new ComponentViewPortNames());
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
              pinViews.add(new ComponentConnection<>((PinView) connectedView, connectionView));
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

  private void addConnectedWire(CircuitInstanceViewPath path, ConnectionView connectionView, WireView connectedView)
  {
    List<WireConnection> wireConnections = connectedWires.get(path);
    if (wireConnections == null)
    {
      wireConnections = new ArrayList<>();
      connectedWires.put(path, wireConnections);
    }
    wireConnections.add(new WireConnection(connectedView, connectionView));
  }

  private void addConnectedComponent(CircuitInstanceViewPath circuitInstanceView,
                                     ConnectionView connectionView,
                                     ComponentView<?> componentView)
  {
    List<ComponentConnection<ComponentView<?>>> componentConnections = connectedComponents.get(circuitInstanceView);
    if (componentConnections == null)
    {
      componentConnections = new ArrayList<>();
      connectedComponents.put(circuitInstanceView, componentConnections);
    }
    componentConnections.add(new ComponentConnection<>(componentView, connectionView));
  }

  protected void addWirePortIndices()
  {
    for (Map.Entry<CircuitInstanceViewPath, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      List<ComponentConnection<ComponentView<?>>> componentConnections = entry.getValue();

      for (ComponentConnection<ComponentView<?>> connectedComponent : componentConnections)
      {
        ComponentView<?> componentView = connectedComponent.component;
        ConnectionView connectionView = connectedComponent.connection;
        PortView portView = componentView.getPortView(connectionView);
        List<String> portNames = portView.getPortNames();

        for (int i = 0; i < componentViewPortNames.size(); i++)
        {
          String portName = portNames.get(i);
          ComponentViewPortNames componentViewPortNames = this.componentViewPortNames.get(i);
          componentViewPortNames.addPort(componentView, portName);
        }
      }
    }
  }

  public Map<CircuitInstanceViewPath, List<WireConnection>> getConnectedWires()
  {
    return connectedWires;
  }

  public List<ComponentViewPortNames> getComponentViewPortNames()
  {
    return componentViewPortNames;
  }

  public List<ConnectionView> getConnectionViews()
  {
    List<ConnectionView> connectionViews = new ArrayList<>(connectedComponents.size());
    for (Map.Entry<CircuitInstanceViewPath, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      List<ComponentConnection<ComponentView<?>>> componentConnections = entry.getValue();
      for (ComponentConnection<ComponentView<?>> componentConnection : componentConnections)
      {
        ConnectionView connectionView = componentConnection.connection;
        connectionViews.add(connectionView);
      }
    }
    return connectionViews;
  }

  public void addTrace(Trace trace)
  {
    traces.add(trace);
  }

  public List<Trace> getTraces()
  {
    return new ArrayList<>(traces);
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

