package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;

import java.util.*;

public class PortTraceFinder
{
  protected Simulation simulation;

  protected List<ComponentConnection<SplitterView>> splitterViewStack;
  protected Map<ConnectionView, SplitterView> processedSplitterViewConnections;
  protected int stackIndex;

  public PortTraceFinder(Simulation simulation)
  {
    this.simulation = simulation;
    splitterViewStack = new ArrayList<>();
    processedSplitterViewConnections = new HashMap<>();
    stackIndex = 0;
  }

  public List<LocalConnectionNet> findAndConnectTraces(ConnectionView inputConnectionView)
  {
    List<LocalConnectionNet> connectionNets = new ArrayList<>();
    findAndConnectTraces(inputConnectionView, connectionNets);
    return connectionNets;
  }

    protected void findAndConnectTraces(ConnectionView inputConnectionView, List<LocalConnectionNet> connectionNets)
  {
    List<FullWire> fullWires = findFullWires(inputConnectionView, connectionNets);

    for (FullWire fullWire : fullWires)
    {
      Trace trace = new Trace();
      Set<PortConnection> localWires = fullWire.getLocalWires();
      for (PortConnection localWire : localWires)
      {
        for (Port port : localWire.connectedPorts)
        {
          port.disconnect(simulation);
          port.connect(trace);
        }
        localWire.localConnectionNet.addTrace(trace);
      }
    }

    for (LocalConnectionNet connectionNet : connectionNets)
    {
      for (WireConnection connectedWire : connectionNet.getConnectedWires())
      {
        WireView wireView = connectedWire.component;
        wireView.connectTraces(connectionNet.getTraces());
      }
    }
  }

  protected List<FullWire> findFullWires(ConnectionView inputConnectionView)
  {
    return findFullWires(inputConnectionView, new ArrayList<>());
  }

  protected List<FullWire> findFullWires(ConnectionView inputConnectionView, List<LocalConnectionNet> connectionNets)
  {
    if (inputConnectionView == null)
    {
      throw new SimulatorException("Input connection may not be null.");
    }

    processLocalConnections(inputConnectionView, connectionNets);

    while (stackIndex < splitterViewStack.size())
    {
      ComponentConnection<SplitterView> splitterViewConnection = splitterViewStack.get(stackIndex);
      ConnectionView connectionView = splitterViewConnection.connection;
      if (!processedSplitterViewConnections.containsKey(connectionView))
      {
        processLocalConnections(connectionView, connectionNets);
      }

      stackIndex++;
    }

    Map<Port, Port> totalSplitterPortMap = createSplitterPortMap(connectionNets);
    Map<Port, PortConnection> totalPortWireMap = createPortWireMap(connectionNets);

    Set<PortConnection> processedWires = new HashSet<>();

    List<FullWire> fullWires = new ArrayList<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      List<PortConnection> portConnections = connectionNet.getPortConnections();
      for (PortConnection portConnection : portConnections)
      {
        if (!processedWires.contains(portConnection))
        {
          FullWire fullWire = new FullWire();
          fullWire.process(portConnection);
          processedWires.add(portConnection);
          while (fullWire.hasPortToProcess())
          {
            Port port = fullWire.getNextPort();
            Port oppositeSplitterPort = totalSplitterPortMap.get(port);
            if (oppositeSplitterPort == null)
            {
              throw new SimulatorException("Could not find opposite port for splitter.");
            }
            PortConnection oppositePortConnection = totalPortWireMap.get(oppositeSplitterPort);
            if (!processedWires.contains(oppositePortConnection) && oppositePortConnection != null)
            {
              fullWire.process(oppositePortConnection);
              processedWires.add(oppositePortConnection);
            }
          }
          fullWire.done();
          fullWires.add(fullWire);
        }
      }
    }
    return fullWires;
  }

  protected Map<Port, Port> createSplitterPortMap(List<LocalConnectionNet> connectionNets)
  {
    Map<Port, Port> totalSplitterPortMap = new HashMap<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      List<ComponentConnection<SplitterView>> splitterViewConnections = connectionNet.getSplitterViews();
      for (ComponentConnection<SplitterView> splitterViewConnection : splitterViewConnections)
      {
        SplitterView splitterView = splitterViewConnection.component;
        Map<Port, Port> portMap = splitterView.getBidirectionalPortMap();
        totalSplitterPortMap.putAll(portMap);
      }
    }
    return totalSplitterPortMap;
  }

  protected Map<Port, PortConnection> createPortWireMap(List<LocalConnectionNet> connectionNets)
  {
    Map<Port, PortConnection> totalPortWireMap = new HashMap<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      List<PortConnection> portConnections = connectionNet.getPortConnections();
      for (PortConnection portConnection : portConnections)
      {
        Set<Port> splitterPorts = portConnection.splitterPorts;
        for (Port splitterPort : splitterPorts)
        {
          totalPortWireMap.put(splitterPort, portConnection);
        }
      }
    }
    return totalPortWireMap;
  }

  protected void processLocalConnections(ConnectionView inputConnectionView, List<LocalConnectionNet> connectionNets)
  {
    LocalConnectionNet connectionNet = new LocalConnectionNet(inputConnectionView);
    connectionNets.add(connectionNet);

    List<ComponentConnection<SplitterView>> splitterViews = connectionNet.getSplitterViews();
    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      SplitterView splitterView = splitterViewConnection.component;
      ConnectionView connection = splitterViewConnection.connection;
      processedSplitterViewConnections.put(connection, splitterView);
    }

    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      SplitterView splitterView = splitterViewConnection.component;

      List<PortView> portViews = splitterView.getPorts();
      for (PortView portView : portViews)
      {
        ConnectionView connectionView = portView.getConnection();
        if (!processedSplitterViewConnections.containsKey(connectionView))
        {
          splitterViewStack.add(new ComponentConnection<>(splitterView, connectionView));
        }
      }
    }
  }

  public List<PortView> getPortViews(List<LocalConnectionNet> connectionNets)
  {
    ArrayList<PortView> portViews = new ArrayList<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      portViews.addAll(connectionNet.getPortViews());
    }
    return portViews;
  }

  public List<ConnectionView> getConnectionViews(List<LocalConnectionNet> connectionNets)
  {
    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      connectionViews.addAll(connectionNet.getConnectionViews());
    }
    return connectionViews;
  }
}

