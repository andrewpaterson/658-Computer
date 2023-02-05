package net.logicim.ui.connection;

import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.port.PortViewFinder;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LocalConnectionNet
{
  protected List<ComponentConnection<ComponentView<?>>> connectedComponents;
  protected List<WireConnection> connectedWires;
  protected List<ComponentConnection<SplitterView>> splitterViews;

  protected List<PortConnection> portConnections;

  protected Set<Trace> traces = new LinkedHashSet<>();

  public LocalConnectionNet(ConnectionView inputConnectionView)
  {
    connectedComponents = new ArrayList<>();
    connectedWires = new ArrayList<>();
    splitterViews = new ArrayList<>();

    ConnectionFinder connectionFinder = new ConnectionFinder();
    connectionFinder.addConnection(inputConnectionView);
    connectionFinder.process();
    Set<ConnectionView> connections = connectionFinder.getConnections();

    int minimumPorts = calculateMinimumPorts(connections);
    if (isValid(minimumPorts))
    {
      portConnections = createPortConnections(minimumPorts);

      findConnections(connections);
      traceConnections();
    }
    else
    {
      portConnections = new ArrayList<>();

      findConnections(connections);
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
      portConnections.add(new PortConnection(this));
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
      PortView portView = componentView.getPort(connection);

      for (int i = 0; i < portConnections.size(); i++)
      {
        PortConnection portConnection = portConnections.get(i);
        Port port = portView.getPort(i);
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

  public List<PortView> getPortViews()
  {
    List<PortView> portViews = new ArrayList<>(connectedComponents.size());
    for (ComponentConnection<ComponentView<?>> connectedComponent : connectedComponents)
    {
      ComponentView<?> componentView = connectedComponent.component;
      ConnectionView connectionView = connectedComponent.connection;

      PortView portView = componentView.getPort(connectionView);
      portViews.add(portView);
    }
    return portViews;
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

  public void addTrace(Trace trace)
  {
    traces.add(trace);
  }

  public List<Trace> getTraces()
  {
    return new ArrayList<>(traces);
  }
}

