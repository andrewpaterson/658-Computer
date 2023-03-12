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

public abstract class PortTraceFinder
{
  public static List<LocalConnectionNet> findAndConnectTraces(Simulation simulation, ConnectionView inputConnectionView)
  {
    List<LocalConnectionNet> connectionNets = new ArrayList<>();
    findAndConnectTraces(simulation, inputConnectionView, connectionNets);
    return connectionNets;
  }

  protected static void findAndConnectTraces(Simulation simulation, ConnectionView inputConnectionView, List<LocalConnectionNet> connectionNets)
  {
    WireList wireList = findWires(inputConnectionView, connectionNets);

    connectFullWires(simulation, wireList.getFullWires());

    List<PartialWire> partialWires = wireList.getPartialWires();
    for (PartialWire partialWire : partialWires)
    {
      for (WireConnection wireConnection : partialWire.connectedWires)
      {
        WireView wireView = wireConnection.getWireView();
        wireView.clearTraces();
      }
    }

    for (LocalConnectionNet connectionNet : connectionNets)
    {
      for (WireConnection connectedWire : connectionNet.getConnectedWires())
      {
        WireView wireView = connectedWire.wireView;
        wireView.connectTraces(connectionNet.getTraces());
      }
    }
  }

  private static void connectFullWires(Simulation simulation, List<FullWire> fullWires)
  {
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
  }

  protected static WireList findWires(ConnectionView inputConnectionView, List<LocalConnectionNet> connectionNets)
  {
    if (inputConnectionView == null)
    {
      throw new SimulatorException("Input connection may not be null.");
    }

    List<ComponentConnection<SplitterView>> splitterViewStack = new ArrayList<>();
    Map<ConnectionView, SplitterView> processedSplitterViewConnections = new HashMap<>();

    processLocalConnections(inputConnectionView, connectionNets, splitterViewStack, processedSplitterViewConnections);

    int stackIndex = 0;
    while (stackIndex < splitterViewStack.size())
    {
      ComponentConnection<SplitterView> splitterViewConnection = splitterViewStack.get(stackIndex);
      ConnectionView connectionView = splitterViewConnection.connection;
      if (!processedSplitterViewConnections.containsKey(connectionView))
      {
        processLocalConnections(connectionView, connectionNets, splitterViewStack, processedSplitterViewConnections);
      }

      stackIndex++;
    }

    return createWireList(connectionNets);
  }

  protected static WireList createWireList(List<LocalConnectionNet> connectionNets)
  {
    Map<Port, Port> totalSplitterPortMap = createSplitterPortMap(connectionNets);
    Map<Port, PortConnection> totalPortWireMap = createPortWireMap(connectionNets);

    Set<PortConnection> processedWires = new HashSet<>();
    WireList wireList = new WireList();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      List<PortConnection> portConnections = connectionNet.getPortConnections();
      if (!portConnections.isEmpty())
      {
        for (PortConnection portConnection : portConnections)
        {
          if (!processedWires.contains(portConnection))
          {
            List<Port> portStack = new ArrayList<>();
            int portStackIndex = 0;

            FullWire fullWire = new FullWire();
            fullWire.process(portConnection, portStack);
            processedWires.add(portConnection);
            while (portStackIndex < portStack.size())
            {
              Port port = portStack.get(portStackIndex);
              portStackIndex++;

              Port oppositeSplitterPort = totalSplitterPortMap.get(port);
              if (oppositeSplitterPort == null)
              {
                throw new SimulatorException("Could not find opposite port for splitter.");
              }
              PortConnection oppositePortConnection = totalPortWireMap.get(oppositeSplitterPort);
              if (!processedWires.contains(oppositePortConnection) && oppositePortConnection != null)
              {
                fullWire.process(oppositePortConnection, portStack);
                processedWires.add(oppositePortConnection);
              }
            }
            wireList.add(fullWire);
          }
        }
      }
      else
      {
        wireList.add(new PartialWire(connectionNet.connectedWires));
      }
    }
    return wireList;
  }

  protected static Map<Port, Port> createSplitterPortMap(List<LocalConnectionNet> connectionNets)
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

  protected static Map<Port, PortConnection> createPortWireMap(List<LocalConnectionNet> connectionNets)
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

  protected static void processLocalConnections(ConnectionView inputConnectionView, List<LocalConnectionNet> connectionNets, List<ComponentConnection<SplitterView>> splitterViewStack, Map<ConnectionView, SplitterView> processedSplitterViewConnections)
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

      List<PortView> portViews = splitterView.getPortViews();
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

  public static List<ConnectionView> getConnectionViews(List<LocalConnectionNet> connectionNets)
  {
    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    for (LocalConnectionNet connectionNet : connectionNets)
    {
      connectionViews.addAll(connectionNet.getConnectionViews());
    }
    return connectionViews;
  }
}

