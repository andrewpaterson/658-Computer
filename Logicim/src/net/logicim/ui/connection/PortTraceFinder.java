package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
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
  public static List<LocalMultiSimulationConnectionNet> findAndConnectTraces(CircuitInstanceView startingCircuitInstanceView,
                                                                             SubcircuitSimulation startingSubcircuitSimulation,
                                                                             ConnectionView inputConnectionView)
  {
    WireList wireList = findWires(startingCircuitInstanceView, startingSubcircuitSimulation, inputConnectionView);

    createTracesAndConnectPorts(wireList);
    disconnectWireViews(wireList);
    connectWireViews(wireList);

    return wireList.getConnectionNets();
  }

  protected static void connectWireViews(WireList wireList)
  {
    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets())
    {
      Set<WireView> processedWireViews = new HashSet<>();
      for (Map.Entry<SubcircuitSimulation, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        SubcircuitSimulation subcircuitSimulation = entry.getKey();
        List<WireConnection> wireConnections = entry.getValue();
        for (WireConnection connectedWire : wireConnections)
        {
          WireView wireView = connectedWire.wireView;
          if (!processedWireViews.contains(wireView))
          {
            wireView.connectTraces(subcircuitSimulation, connectionNet.getTraces());
            processedWireViews.add(wireView);
          }
        }
      }
    }
  }

  protected static void disconnectWireViews(WireList wireList)
  {
    for (PartialWire partialWire : wireList.getPartialWires())
    {
      Map<SubcircuitSimulation, List<WireConnection>> connectedWires = partialWire.connectedWires;
      for (Map.Entry<SubcircuitSimulation, List<WireConnection>> entry : connectedWires.entrySet())
      {
        SubcircuitSimulation subcircuitSimulation = entry.getKey();
        List<WireConnection> wireConnections1 = entry.getValue();
        for (WireConnection wireConnection : wireConnections1)
        {
          WireView wireView = wireConnection.getWireView();
          wireView.disconnect(subcircuitSimulation);
        }
      }
    }

    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets())
    {
      for (Map.Entry<SubcircuitSimulation, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        SubcircuitSimulation subcircuitSimulation = entry.getKey();
        List<WireConnection> wireConnections = entry.getValue();
        for (WireConnection connectedWire : wireConnections)
        {
          WireView wireView = connectedWire.wireView;
          wireView.disconnect(subcircuitSimulation);
        }
      }
    }
  }

  private static void createTracesAndConnectPorts(WireList wireList)
  {
    List<FullWire> fullWires = wireList.getFullWires();
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

  private static WireList findWires(CircuitInstanceView startingCircuitInstanceView, SubcircuitSimulation startingSubcircuitSimulation, ConnectionView inputConnectionView)
  {
    if (inputConnectionView == null)
    {
      throw new SimulatorException("Input connection may not be null.");
    }

    //Rewrite this to be non-retarded.
    List<ComponentSimulationConnection<SplitterView>> splitterViewStack = new ArrayList<>();
    Map<ConnectionView, SplitterView> processedSplitterViewConnections = new HashMap<>();
    Set<SubcircuitPinView> processedSubcircuitInstanceViews = new HashSet<>();

    List<LocalMultiSimulationConnectionNet> connectionNets = new ArrayList<>();
    processLocalMultiSimulationConnections(startingCircuitInstanceView,
                                           startingSubcircuitSimulation,
                                           inputConnectionView,
                                           connectionNets,
                                           splitterViewStack,
                                           processedSplitterViewConnections,
                                           processedSubcircuitInstanceViews);

    int stackIndex = 0;
    while (stackIndex < splitterViewStack.size())
    {
      ComponentSimulationConnection<SplitterView> splitterViewConnection = splitterViewStack.get(stackIndex);
      ConnectionView connectionView = splitterViewConnection.connection;
      if (!processedSplitterViewConnections.containsKey(connectionView))
      {
        processLocalMultiSimulationConnections(splitterViewConnection.circuitInstanceView,
                                               splitterViewConnection.subcircuitSimulation,
                                               connectionView,
                                               connectionNets,
                                               splitterViewStack,
                                               processedSplitterViewConnections,
                                               processedSubcircuitInstanceViews);
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
    WireList wireList = new WireList(connectionNets);
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
        List<ComponentSimulationConnection<SplitterView>> splitterViews = localConnectionNet.getSplitterViews();
        for (ComponentSimulationConnection<SplitterView> componentConnection : splitterViews)
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

  private static void processLocalMultiSimulationConnections(CircuitInstanceView startingCircuitInstanceView,
                                                             SubcircuitSimulation startingSubcircuitSimulation,
                                                             ConnectionView inputConnectionView,
                                                             List<LocalMultiSimulationConnectionNet> connectionNets,
                                                             List<ComponentSimulationConnection<SplitterView>> splitterViewStack,
                                                             Map<ConnectionView, SplitterView> processedSplitterViewConnections,
                                                             Set<SubcircuitPinView> processedSubcircuitPinViews)
  {
    LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet = new LocalMultiSimulationConnectionNet();
    connectionNets.add(localMultiSimulationConnectionNet);

    List<LocalConnectionToProcess> localConnectionsToProcess = new ArrayList<>();
    localConnectionsToProcess.add(new LocalConnectionToProcess(startingCircuitInstanceView, startingSubcircuitSimulation, inputConnectionView));

    while (localConnectionsToProcess.size() > 0)
    {
      LocalConnectionToProcess localConnectionToProcess = localConnectionsToProcess.remove(0);
      processLocalConnections(localConnectionToProcess,
                              localConnectionsToProcess,
                              localMultiSimulationConnectionNet,
                              splitterViewStack,
                              processedSplitterViewConnections,
                              processedSubcircuitPinViews);
    }
  }

  private static void processLocalConnections(LocalConnectionToProcess localConnectionToProcess,
                                              List<LocalConnectionToProcess> localConnectionsToProcess,
                                              LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet,
                                              List<ComponentSimulationConnection<SplitterView>> splitterViewStack,
                                              Map<ConnectionView, SplitterView> processedSplitterViewConnections,
                                              Set<SubcircuitPinView> processedSubcircuitPinViews)
  {
    LocalConnectionNet localConnectionNet = new LocalConnectionNet(localConnectionToProcess.subcircuitSimulation,
                                                                   localConnectionToProcess.circuitInstanceView,
                                                                   localMultiSimulationConnectionNet
    );
    localConnectionNet.process(localConnectionToProcess.inputConnectionView);

    splitterViewStack.addAll(createSplitterViewComponentConnections(localConnectionNet, processedSplitterViewConnections));

    for (ComponentConnection<SubcircuitInstanceView> componentConnection : localConnectionNet.subcircuitInstanceViews)
    {
      SubcircuitInstanceView subcircuitInstanceView = componentConnection.component;

      SubcircuitPinView subcircuitPinView = subcircuitInstanceView.getSubcircuitPinView(componentConnection.connection);
      if (subcircuitPinView == null)
      {
        throw new SimulatorException("Subcircuit pin view is [null] for subcircuit instance view [%s] connection [%s].",
                                     subcircuitInstanceView.getDescription(),
                                     componentConnection.connection.toString());
      }
      processedSubcircuitPinViews.add(subcircuitPinView);

      ConnectionView pinConnection = getPinConnectionView(subcircuitInstanceView, componentConnection.connection);

      SubcircuitInstance subcircuitInstance = subcircuitInstanceView.getComponent(localConnectionToProcess.subcircuitSimulation);
      if (subcircuitInstance == null)
      {
        throw new SimulatorException("Subcircuit instance view [%s] component is [null] for simulation [%s].",
                                     subcircuitInstanceView.getDescription(),
                                     localConnectionToProcess.subcircuitSimulation.getDescription());
      }
      SubcircuitInstanceSimulation subcircuitInstanceSimulation = subcircuitInstance.getSubcircuitInstanceSimulation();
      localConnectionsToProcess.add(new LocalConnectionToProcess(localConnectionToProcess.circuitInstanceView,
                                                                 subcircuitInstanceSimulation,
                                                                 pinConnection));
    }

    for (ComponentConnection<PinView> componentConnection : localConnectionNet.pinViews)
    {
      PinView pinView = componentConnection.component;
      List<SubcircuitPinView> subcircuitPinViews = pinView.getSubcircuitPinViews();
      for (SubcircuitPinView subcircuitPinView : subcircuitPinViews)
      {
        //I'm not sure this check is good enough for partial wires and splitter views.
        if (!processedSubcircuitPinViews.contains(subcircuitPinView))
        {
          processedSubcircuitPinViews.add(subcircuitPinView);

          SubcircuitInstanceView subcircuitInstanceView = subcircuitPinView.getSubcircuitInstanceView();
          SubcircuitSimulation outerSubcircuitSimulation = subcircuitInstanceView.getComponentSubcircuitSimulation(localConnectionToProcess.subcircuitSimulation.getCircuitSimulation());
          if (outerSubcircuitSimulation != null)
          {
            ConnectionView subcircuitInstanceConnection = subcircuitPinView.getConnection();
            localConnectionsToProcess.add(new LocalConnectionToProcess(subcircuitInstanceView,
                                                                       outerSubcircuitSimulation,
                                                                       subcircuitInstanceConnection));
          }
        }
      }
    }
  }

  private static ConnectionView getPinConnectionView(SubcircuitInstanceView subcircuitInstanceView, ConnectionView connectionView)
  {
    SubcircuitPinView subcircuitPinView = subcircuitInstanceView.getSubcircuitPinView(connectionView);
    PinView pinView = subcircuitPinView.getPinView();
    PortView portView = pinView.getPortView();
    return portView.getConnection();
  }

  private static List<ComponentSimulationConnection<SplitterView>> createSplitterViewComponentConnections(LocalConnectionNet connectionNet,
                                                                                                          Map<ConnectionView, SplitterView> processedSplitterViewConnections)
  {
    List<ComponentSimulationConnection<SplitterView>> splitterComponentsConnections = new ArrayList<>();
    List<ComponentSimulationConnection<SplitterView>> splitterViews = connectionNet.getSplitterViews();
    if (splitterViews.size() > 0)
    {
      for (ComponentSimulationConnection<SplitterView> splitterViewConnection : splitterViews)
      {
        SplitterView splitterView = splitterViewConnection.component;
        ConnectionView connection = splitterViewConnection.connection;
        processedSplitterViewConnections.put(connection, splitterView);
      }

      for (ComponentSimulationConnection<SplitterView> splitterViewConnection : splitterViews)
      {
        SplitterView splitterView = splitterViewConnection.component;
        SubcircuitSimulation subcircuitSimulation = splitterViewConnection.subcircuitSimulation;
        CircuitInstanceView circuitInstanceView = splitterViewConnection.circuitInstanceView;

        List<PortView> portViews = splitterView.getPortViews();
        for (PortView portView : portViews)
        {
          ConnectionView connectionView = portView.getConnection();
          if (!processedSplitterViewConnections.containsKey(connectionView))
          {
            splitterComponentsConnections.add(new ComponentSimulationConnection<>(splitterView,
                                                                                  subcircuitSimulation,
                                                                                  circuitInstanceView,
                                                                                  connectionView));
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

