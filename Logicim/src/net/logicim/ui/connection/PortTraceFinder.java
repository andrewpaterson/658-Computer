package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitPinView;

import java.util.*;

public abstract class PortTraceFinder
{
  public static List<LocalMultiSimulationConnectionNet> findAndConnectTraces(SubcircuitSimulation startingSubcircuitSimulation,
                                                                             ConnectionView inputConnectionView)
  {
    List<LocalMultiSimulationConnectionNet> connectionNets = new ArrayList<>();
    WireList wireList = findWires(startingSubcircuitSimulation, inputConnectionView, connectionNets);

    connectFullWires(wireList.getFullWires());

    List<PartialWire> partialWires = wireList.getPartialWires();
    for (PartialWire partialWire : partialWires)
    {
      for (WireConnection wireConnection : partialWire.connectedWires)
      {
        WireView wireView = wireConnection.getWireView();
       //xxx // This is wrong.  It can't be the starting simulation.  It must be the simulation form the subcircuit the wire is in.
        wireView.clearTraces(startingSubcircuitSimulation);
      }
    }

    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      for (WireConnection connectedWire : connectionNet.getConnectedWires())
      {
        WireView wireView = connectedWire.wireView;
        //xxx // This is wrong.  It can't be the starting simulation.  It must be the simulation form the subcircuit the wire is in.
        wireView.connectTraces(startingSubcircuitSimulation, connectionNet.getTraces());
      }
    }
    return connectionNets;
  }

  private static void connectFullWires(List<FullWire> fullWires)
  {
    for (FullWire fullWire : fullWires)
    {
      Trace trace = new Trace();
      Set<PortConnection> localWires = fullWire.getLocalWires();
      for (PortConnection localWire : localWires)
      {
        LocalMultiSimulationConnectionNet multiSimulationConnectionNet = localWire.getMultiSimulationConnectionNet();

        Map<SubcircuitSimulation, List<Port>> connectedPorts = localWire.getConnectedPorts();
        for (Map.Entry<SubcircuitSimulation, List<Port>> entry : connectedPorts.entrySet())
        {
          SubcircuitSimulation subcircuitSimulation = entry.getKey();
          List<Port> ports = entry.getValue();
          for (Port port : ports)
          {
            port.disconnect(subcircuitSimulation.getSimulation());
            port.connect(trace);
          }
        }
        multiSimulationConnectionNet.addTrace(trace);
      }
    }
  }

  private static WireList findWires(SubcircuitSimulation startingSubcircuitSimulation,
                                    ConnectionView inputConnectionView,
                                    List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    if (inputConnectionView == null)
    {
      throw new SimulatorException("Input connection may not be null.");
    }

    List<ComponentConnection<SplitterView>> splitterViewStack = new ArrayList<>();
    Map<ConnectionView, SplitterView> processedSplitterViewConnections = new HashMap<>();

    processLocalMultiSimulationConnections(startingSubcircuitSimulation,
                                           inputConnectionView,
                                           connectionNets,
                                           splitterViewStack,
                                           processedSplitterViewConnections);

    int stackIndex = 0;
    while (stackIndex < splitterViewStack.size())
    {
      ComponentConnection<SplitterView> splitterViewConnection = splitterViewStack.get(stackIndex);
      ConnectionView connectionView = splitterViewConnection.connection;
      if (!processedSplitterViewConnections.containsKey(connectionView))
      {
        processLocalMultiSimulationConnections(startingSubcircuitSimulation,
                                               connectionView,
                                               connectionNets,
                                               splitterViewStack,
                                               processedSplitterViewConnections);
      }

      stackIndex++;
    }

    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      connectionNet.process();
    }

    return createWireList(connectionNets);
  }

  private static WireList createWireList(List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    Map<Port, Port> totalSplitterPortMap = createSplitterPortMap(connectionNets);
    Map<Port, PortConnection> totalPortWireMap = createPortWireMap(connectionNets);

    Set<PortConnection> processedWires = new HashSet<>();
    WireList wireList = new WireList();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
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
        wireList.add(new PartialWire(connectionNet.getConnectedWires()));
      }
    }
    return wireList;
  }

  private static Map<Port, Port> createSplitterPortMap(List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    Map<Port, Port> totalSplitterPortMap = new HashMap<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      for (LocalConnectionNet localConnectionNet : connectionNet.getLocalConnectionNets())
      {
        List<ComponentConnection<SplitterView>> splitterViews = localConnectionNet.getSplitterViews();
        for (ComponentConnection<SplitterView> componentConnection : splitterViews)
        {
          SplitterView splitterView = componentConnection.component;
          Map<Port, Port> portMap = splitterView.getSimulationBidirectionalPorts(localConnectionNet.getSubcircuitSimulation());
          if (portMap != null)
          {
            totalSplitterPortMap.putAll(portMap);
          }
        }
      }
    }

    return totalSplitterPortMap;
  }

  private static Map<Port, PortConnection> createPortWireMap(List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    Map<Port, PortConnection> totalPortWireMap = new HashMap<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
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

  private static void processLocalMultiSimulationConnections(SubcircuitSimulation subcircuitSimulation,
                                                             ConnectionView inputConnectionView,
                                                             List<LocalMultiSimulationConnectionNet> connectionNets,
                                                             List<ComponentConnection<SplitterView>> splitterViewStack,
                                                             Map<ConnectionView, SplitterView> processedSplitterViewConnections)
  {
    LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet = new LocalMultiSimulationConnectionNet();
    connectionNets.add(localMultiSimulationConnectionNet);

    processLocalConnections(subcircuitSimulation, inputConnectionView, localMultiSimulationConnectionNet, splitterViewStack, processedSplitterViewConnections);
  }

  private static void processLocalConnections(SubcircuitSimulation subcircuitSimulation,
                                              ConnectionView inputConnectionView,
                                              LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet,
                                              List<ComponentConnection<SplitterView>> splitterViewStack,
                                              Map<ConnectionView, SplitterView> processedSplitterViewConnections)
  {
    LocalConnectionNet connectionNet = new LocalConnectionNet(subcircuitSimulation, localMultiSimulationConnectionNet, inputConnectionView);
    splitterViewStack.addAll(createSplitterViewComponentConnections(connectionNet, processedSplitterViewConnections));

    for (ComponentConnection<SubcircuitInstanceView> componentConnection : connectionNet.subcircuitInstanceViews)
    {
      SubcircuitInstanceView subcircuitInstanceView = componentConnection.component;
      ConnectionView pinConnection = getPinConnectionView(componentConnection, subcircuitInstanceView);

      SubcircuitInstance subcircuitInstance = subcircuitInstanceView.getComponent(subcircuitSimulation);
      SubcircuitInstanceSimulation subcircuitInstanceSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();

      processLocalConnections(subcircuitInstanceSimulation,
                              pinConnection,
                              localMultiSimulationConnectionNet,
                              splitterViewStack,
                              processedSplitterViewConnections);
    }
  }

  private static ConnectionView getPinConnectionView(ComponentConnection<SubcircuitInstanceView> componentConnection, SubcircuitInstanceView subcircuitInstanceView)
  {
    ConnectionView connectionView = componentConnection.connection;
    SubcircuitPinView subcircuitPinView = subcircuitInstanceView.getSubcircuitPinView(connectionView);
    PinView pinView = subcircuitPinView.getPinView();
    PortView portView = pinView.getPortView();
    return portView.getConnection();
  }

  private static List<ComponentConnection<SplitterView>> createSplitterViewComponentConnections(LocalConnectionNet connectionNet, Map<ConnectionView, SplitterView> processedSplitterViewConnections)
  {
    List<ComponentConnection<SplitterView>> splitterComponentsConnections = new ArrayList<>();
    List<ComponentConnection<SplitterView>> splitterViews = connectionNet.getSplitterViews();
    if (splitterViews.size() > 0)
    {
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
            splitterComponentsConnections.add(new ComponentConnection<>(splitterView, connectionView));
          }
        }
      }
    }

    return splitterComponentsConnections;
  }

  public static List<ConnectionView> getConnectionViews(List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      connectionViews.addAll(connectionNet.getConnectionViews());
    }
    return connectionViews;
  }
}

