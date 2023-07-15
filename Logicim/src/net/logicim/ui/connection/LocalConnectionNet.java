package net.logicim.ui.connection;

import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.port.PortViewFinder;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LocalConnectionNet
{
  protected List<ComponentConnection<ComponentView<?>>> connectedComponents;
  protected List<WireConnection> connectedWires;
  protected List<ComponentConnection<SplitterView>> splitterViews;
  protected List<ComponentConnection<SubcircuitInstanceView>> subcircuitInstanceViews;
  protected List<ComponentConnection<PinView>> pinViews;

  protected List<PortConnection> portConnections;

  protected LocalMultiSimulationConnectionNet multiSimulationNet;
  protected SubcircuitSimulation subcircuitSimulation;

  public LocalConnectionNet(SubcircuitSimulation subcircuitSimulation, LocalMultiSimulationConnectionNet multiSimulationNet, ConnectionView inputConnectionView)
  {
    this.subcircuitSimulation = subcircuitSimulation;
    this.multiSimulationNet = multiSimulationNet;
    this.multiSimulationNet.add(this);

    this.connectedComponents = new ArrayList<>();
    this.connectedWires = new ArrayList<>();
    this.splitterViews = new ArrayList<>();
    this.subcircuitInstanceViews = new ArrayList<>();
    this.pinViews = new ArrayList<>();

    ConnectionFinder connectionFinder = new ConnectionFinder();
    connectionFinder.addConnection(inputConnectionView);
    connectionFinder.process();
    Set<ConnectionView> connectionViews = connectionFinder.getConnections();

    int minimumPorts = calculateMinimumPorts(connectionViews);
    if (isValid(minimumPorts))
    {
      portConnections = createPortConnections(minimumPorts);

      findConnections(connectionViews);
      traceConnections();
    }
    else
    {
      portConnections = new ArrayList<>();

      findConnections(connectionViews);
    }
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
      portConnections.add(new PortConnection(multiSimulationNet));
    }
    return portConnections;
  }

  protected int calculateMinimumPorts(Set<ConnectionView> connections)
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

  protected void findConnections(Set<ConnectionView> connections)
  {
    for (ConnectionView connectionView : connections)
    {
      List<View> localConnected = connectionView.getConnectedComponents();
      for (View connectedView : localConnected)
      {
        if (connectedView instanceof ComponentView)
        {
          ComponentView<?> componentView = (ComponentView<?>) connectedView;
          if (componentView instanceof SplitterView)
          {
            splitterViews.add(new ComponentConnection<>((SplitterView) componentView, connectionView));
          }
          else if (connectedView instanceof PinView)
          {
            pinViews.add(new ComponentConnection<>((PinView) connectedView, connectionView));
          }
          else if (connectedView instanceof SubcircuitInstanceView)
          {
            subcircuitInstanceViews.add(new ComponentConnection<>((SubcircuitInstanceView) connectedView, connectionView));
          }

          connectedComponents.add(new ComponentConnection<>(componentView, connectionView));
        }
        else if (connectedView instanceof WireView)
        {
          connectedWires.add(new WireConnection((WireView) connectedView, connectionView));
        }
      }
    }
  }

  protected void traceConnections()
  {
    for (ComponentConnection<ComponentView<?>> connectedComponent : connectedComponents)
    {
      ComponentView<?> componentView = connectedComponent.component;
      ConnectionView connection = connectedComponent.connection;

      boolean isSplitter = componentView instanceof SplitterView;
      PortView portView = componentView.getPortView(connection);

      for (int i = 0; i < portConnections.size(); i++)
      {
        PortConnection portConnection = portConnections.get(i);
        Port port = portView.getPort(subcircuitSimulation, i);
        portConnection.addPort(port);
        if (isSplitter)
        {
          portConnection.addSplitterPort(port);
        }
      }
    }
  }

  public List<WireConnection> getConnectedWires()
  {
    return connectedWires;
  }

  public List<ComponentConnection<SplitterView>> getSplitterViews()
  {
    return splitterViews;
  }

  public List<PortConnection> getPortConnections()
  {
    return portConnections;
  }

  public List<ConnectionView> getConnectionViews()
  {
    List<ConnectionView> connectionViews = new ArrayList<>(connectedComponents.size());
    for (ComponentConnection<ComponentView<?>> connectedComponent : connectedComponents)
    {
      ConnectionView connectionView = connectedComponent.connection;
      connectionViews.add(connectionView);
    }
    return connectionViews;
  }

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    for (ComponentConnection<ComponentView<?>> connectedComponent : connectedComponents)
    {
      ComponentView<?> component = connectedComponent.component;
      ConnectionView connection = connectedComponent.connection;

      builder.append(component.getType() + " (" + connection.getGridPosition() + ")@" + System.identityHashCode(connection) + "\n");
    }
    return builder.toString();
  }
}

