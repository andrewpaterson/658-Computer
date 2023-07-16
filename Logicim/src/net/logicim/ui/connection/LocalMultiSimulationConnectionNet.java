package net.logicim.ui.connection;

import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.port.PortViewFinder;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.*;

public class LocalMultiSimulationConnectionNet
{
  protected List<LocalConnectionNet> localConnectionNets;

  protected Set<Trace> traces = new LinkedHashSet<>();
  protected Map<SubcircuitSimulation, List<ComponentConnection<ComponentView<?>>>> connectedComponents;
  protected List<WireConnection> connectedWires;
  protected List<ComponentConnection<PinView>> pinViews;

  protected List<PortConnection> portConnections;

  public LocalMultiSimulationConnectionNet()
  {
    this.localConnectionNets = new ArrayList<>();
    this.connectedComponents = new LinkedHashMap<>();
    this.connectedWires = new ArrayList<>();
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
      portConnections = createPortConnections(minimumPorts);

      findConnections(localConnectionNets);
      traceConnections();
    }
    else
    {
      portConnections = new ArrayList<>();

      findConnections(localConnectionNets);
    }
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

  private boolean isValid(int minimumPorts)
  {
    return minimumPorts != Integer.MAX_VALUE;
  }

  protected List<PortConnection> createPortConnections(int minimumPorts)
  {
    List<PortConnection> portConnections = new ArrayList<>(minimumPorts);
    for (int i = 0; i < minimumPorts; i++)
    {
      portConnections.add(new PortConnection(this));
    }
    return portConnections;
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
      SubcircuitSimulation subcircuitSimulation = localConnectionNet.getSubcircuitSimulation();
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

            List<ComponentConnection<ComponentView<?>>> componentConnections = connectedComponents.get(subcircuitSimulation);
            if (componentConnections == null)
            {
              componentConnections = new ArrayList<>();
              connectedComponents.put(subcircuitSimulation, componentConnections);
            }
            componentConnections.add(new ComponentConnection<>(componentView, connectionView));
          }
          else if (connectedView instanceof WireView)
          {
            connectedWires.add(new WireConnection((WireView) connectedView, connectionView));
          }
        }
      }
    }
  }

  protected void traceConnections()
  {
    for (Map.Entry<SubcircuitSimulation, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
    {
      SubcircuitSimulation subcircuitSimulation = entry.getKey();
      List<ComponentConnection<ComponentView<?>>> componentConnections = entry.getValue();

      for (ComponentConnection<ComponentView<?>> connectedComponent : componentConnections)
      {
        ComponentView<?> componentView = connectedComponent.component;
        ConnectionView connection = connectedComponent.connection;

        boolean isSplitter = componentView instanceof SplitterView;
        PortView portView = componentView.getPortView(connection);

        for (int i = 0; i < portConnections.size(); i++)
        {
          PortConnection portConnection = portConnections.get(i);
          Port port = portView.getPort(subcircuitSimulation, i);
          portConnection.addPort(subcircuitSimulation, port);
          if (isSplitter)
          {
            portConnection.addSplitterPort(port);
          }
        }
      }
    }
  }

  public List<WireConnection> getConnectedWires()
  {
    return connectedWires;
  }

  public List<PortConnection> getPortConnections()
  {
    return portConnections;
  }

  public List<ComponentConnection<SplitterView>> getSplitterViews()
  {
    List<ComponentConnection<SplitterView>> result = new ArrayList<>();
    for (LocalConnectionNet localConnectionNet : localConnectionNets)
    {
      List<ComponentConnection<SplitterView>> splitterViews = localConnectionNet.getSplitterViews();
      result.addAll(splitterViews);
    }
    return result;
  }

  public List<ConnectionView> getConnectionViews()
  {
    List<ConnectionView> connectionViews = new ArrayList<>(connectedComponents.size());
    for (Map.Entry<SubcircuitSimulation, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
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

    for (Map.Entry<SubcircuitSimulation, List<ComponentConnection<ComponentView<?>>>> entry : connectedComponents.entrySet())
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

