package net.logicim.ui.connection;

import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.integratedcircuit.standard.passive.splitter.SplitterView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LocalConnectionNet
{
  protected List<ComponentConnection<ComponentView<?>>> connectedComponents;
  protected List<ComponentConnection<WireView>> connectedWires;
  protected List<ComponentConnection<SplitterView>> splitterViews;

  protected List<WireConnection> wireConnections;

  protected Set<Trace> traces = new LinkedHashSet<>();

  public LocalConnectionNet(ConnectionView inputConnectionView)
  {
    connectedComponents = new ArrayList<>();
    connectedWires = new ArrayList<>();
    splitterViews = new ArrayList<>();

    ConnectionFinder connectionFinder = new ConnectionFinder();
    connectionFinder.findConnections(inputConnectionView);
    Set<ConnectionView> connections = connectionFinder.getConnections();

    int minimumPorts = calculateMinimumPorts(connections);
    if (isValid(minimumPorts))
    {
      wireConnections = createPortConnections(minimumPorts);

      findConnections(connections);
      traceConnections();
    }
    else
    {
      wireConnections = new ArrayList<>();
    }
  }

  private boolean isValid(int minimumPorts)
  {
    return minimumPorts != Integer.MAX_VALUE;
  }

  protected List<WireConnection> createPortConnections(int minimumPorts)
  {
    List<WireConnection> wireConnections = new ArrayList<>(minimumPorts);
    for (int i = 0; i < minimumPorts; i++)
    {
      wireConnections.add(new WireConnection(this));
    }
    return wireConnections;
  }

  protected int calculateMinimumPorts(Set<ConnectionView> connections)
  {
    int minimumPorts = Integer.MAX_VALUE;
    for (ConnectionView connectionView : connections)
    {
      List<View> connectedComponents = connectionView.getConnectedComponents();
      for (View connectedView : connectedComponents)
      {
        if (connectedView instanceof ComponentView)
        {
          ComponentView<?> componentView = (ComponentView<?>) connectedView;
          PortView portView = componentView.getPort(connectionView);
          if (portView.numberOfPorts() < minimumPorts)
          {
            minimumPorts = portView.numberOfPorts();
            if (minimumPorts == 1)
            {
              break;
            }
          }
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
          connectedWires.add(new ComponentConnection<>((WireView) connectedView, connectionView));
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

      for (int i = 0; i < wireConnections.size(); i++)
      {
        WireConnection portConnection = wireConnections.get(i);
        Port port = portView.getPort(i);
        portConnection.addPort(port);
        if (isSplitter)
        {
          portConnection.addSplitterPort(port);
        }
      }
    }
  }

  public List<ComponentConnection<ComponentView<?>>> getConnectedComponents()
  {
    return connectedComponents;
  }

  public List<ComponentConnection<WireView>> getConnectedWires()
  {
    return connectedWires;
  }

  public List<ComponentConnection<SplitterView>> getSplitterViews()
  {
    return splitterViews;
  }

  public List<WireConnection> getWireConnections()
  {
    return wireConnections;
  }

  public List<PortView> getPortViews()
  {
    List<PortView> portViews = new ArrayList<>(connectedComponents.size());
    for (ComponentConnection<ComponentView<?>> connectedComponent : connectedComponents)
    {
      ComponentView<?> componentView = connectedComponent.component;
      ConnectionView connection = connectedComponent.connection;

      PortView portView = componentView.getPort(connection);
      portViews.add(portView);
    }
    return portViews;
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

