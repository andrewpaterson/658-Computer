package net.logicim.ui.connection;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.ComponentView;
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
    WireList wireList = findWires(startingCircuitInstanceView, inputConnectionView);

    createTracesAndConnectPorts(startingSubcircuitSimulation, wireList);
    disconnectWireViews(wireList);
    connectWireViews(wireList);

    return wireList.getConnectionNets();
  }

  protected static void connectWireViews(WireList wireList)
  {
    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets())
    {
      Set<WireView> processedWireViews = new HashSet<>();
      for (Map.Entry<CircuitInstanceView, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        CircuitInstanceView subcircuitSimulation = entry.getKey();
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
      Map<CircuitInstanceView, List<WireConnection>> connectedWires = partialWire.connectedWires;
      for (Map.Entry<CircuitInstanceView, List<WireConnection>> entry : connectedWires.entrySet())
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
      for (Map.Entry<CircuitInstanceView, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        CircuitInstanceView subcircuitSimulation = entry.getKey();
        List<WireConnection> wireConnections = entry.getValue();
        for (WireConnection connectedWire : wireConnections)
        {
          WireView wireView = connectedWire.wireView;
          wireView.disconnect(subcircuitSimulation);
        }
      }
    }
  }

  private static void createTracesAndConnectPorts(SubcircuitSimulation startingSubcircuitSimulation, WireList wireList)
  {
    Simulation simulation = startingSubcircuitSimulation.getSimulation();

    List<FullWire> fullWires = wireList.getFullWires();
    for (FullWire fullWire : fullWires)
    {
      Trace trace = new Trace();
      Set<ComponentViewPortNames> localWires = fullWire.getLocalWires();
      for (ComponentViewPortNames localWire : localWires)
      {
        LocalMultiSimulationConnectionNet multiSimulationConnectionNet = localWire.getMultiSimulationConnectionNet();

        List<ComponentViewPortName> connectedPortIndices = localWire.getConnectedPortIndices();
        for (ComponentViewPortName connectedPortIndex : connectedPortIndices)
        {
          ComponentView<?> componentView = connectedPortIndex.componentView;
          String portName = connectedPortIndex.portName;
          Port port = componentView.getPort(startingSubcircuitSimulation, portName);
          port.disconnect(simulation);
          port.connect(trace);
        }

        multiSimulationConnectionNet.addTrace(trace);
      }
    }
  }

  private static WireList findWires(CircuitInstanceView startingCircuitInstanceView,
                                    ConnectionView startingConnectionView)
  {
    if (startingConnectionView == null)
    {
      throw new SimulatorException("Input connection may not be null.");
    }

    List<SplitterProcessStackItem> splitterViewStack = new ArrayList<>();
    SplitterProcessStackItem startingSplitterlessConnection = new SplitterProcessStackItem(null, startingCircuitInstanceView, startingConnectionView);
    splitterViewStack.add(startingSplitterlessConnection);

    Set<ConnectionView> processedSplitterViewConnections = new HashSet<>();
    Set<SubcircuitPinView> processedSubcircuitInstanceViews = new HashSet<>();
    List<LocalMultiSimulationConnectionNet> connectionNets = new ArrayList<>();

    while (splitterViewStack.size() > 0)
    {
      SplitterProcessStackItem splitterViewConnection = splitterViewStack.remove(0);
      ConnectionView connectionView = splitterViewConnection.connection;
      if (!processedSplitterViewConnections.contains(connectionView))
      {
        processLocalMultiSimulationConnections(splitterViewConnection.circuitInstanceView,
                                               connectionView,
                                               connectionNets,
                                               splitterViewStack,
                                               processedSplitterViewConnections,
                                               processedSubcircuitInstanceViews);
      }
    }

    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      connectionNet.process();
    }

    return createWireList(connectionNets);
  }

  private static WireList createWireList(List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap = createSplitterPortMap(connectionNets);

    Set<ComponentViewPortNames> processedComponentViewPortNames = new HashSet<>();
    WireList wireList = new WireList(connectionNets);
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      List<ComponentViewPortNames> componentViewPortNames = connectionNet.getComponentViewPortNames();
      if (!componentViewPortNames.isEmpty())
      {
        List<FullWire> fullWires = createFullWires(totalPortWireMap, processedComponentViewPortNames, componentViewPortNames);
        wireList.add(fullWires);
      }
      else
      {
        wireList.add(new PartialWire(connectionNet.getConnectedWires()));
      }
    }
    return wireList;
  }

  private static List<FullWire> createFullWires(Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap,
                                                Set<ComponentViewPortNames> processedComponentViewPortNames,
                                                List<ComponentViewPortNames> componentViewPortNamesList)
  {
    List<FullWire> fullWires = new ArrayList<>();
    for (ComponentViewPortNames componentViewPortNames : componentViewPortNamesList)
    {
      if (!processedComponentViewPortNames.contains(componentViewPortNames))
      {
        FullWire fullWire = createFullWire(totalPortWireMap, processedComponentViewPortNames, componentViewPortNames);
        fullWires.add(fullWire);
      }
    }
    return fullWires;
  }

  private static FullWire createFullWire(Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap,
                                         Set<ComponentViewPortNames> processedComponentViewPortNames,
                                         ComponentViewPortNames componentViewPortNames)
  {
    List<ComponentViewPortName> portIndexStack = new ArrayList<>();
    FullWire fullWire = new FullWire();
    fullWire.process(componentViewPortNames, portIndexStack);
    processedComponentViewPortNames.add(componentViewPortNames);
    int portStackIndex = 0;
    while (portStackIndex < portIndexStack.size())
    {
      ComponentViewPortName componentViewPortName = portIndexStack.get(portStackIndex);
      portStackIndex++;

      SplitterView splitterView = (SplitterView) componentViewPortName.componentView;
      String oppositeSplitterPort = splitterView.getOpposite(componentViewPortName.portName);

      if (oppositeSplitterPort == null)
      {
        throw new SimulatorException("Could not find opposite port of [%s] for Splitter View[%s].",
                                     componentViewPortName.portName,
                                     splitterView.getDescription());
      }

      ComponentViewPortNames oppositeComponentViewPortNames = getOppositePortConnection(totalPortWireMap, splitterView, oppositeSplitterPort);

      if (!processedComponentViewPortNames.contains(oppositeComponentViewPortNames) && oppositeComponentViewPortNames != null)
      {
        fullWire.process(oppositeComponentViewPortNames, portIndexStack);
        processedComponentViewPortNames.add(oppositeComponentViewPortNames);
      }
    }
    return fullWire;
  }

  private static ComponentViewPortNames getOppositePortConnection(Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap, SplitterView splitterView, String oppositeSplitterPort)
  {
    ComponentViewPortNames oppositeComponentViewPortNames = null;
    for (Map.Entry<ComponentViewPortName, ComponentViewPortNames> entry : totalPortWireMap.entrySet())
    {
      ComponentViewPortName key = entry.getKey();
      if (key.componentView == splitterView)
      {
        if (key.portName.equals(oppositeSplitterPort))
        {
          oppositeComponentViewPortNames = entry.getValue();
        }
      }
    }
    return oppositeComponentViewPortNames;
  }

  private static Map<ComponentViewPortName, ComponentViewPortNames> createSplitterPortMap(List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap = new HashMap<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      List<ComponentViewPortNames> componentViewPortNamesList = connectionNet.getComponentViewPortNames();
      for (ComponentViewPortNames componentViewPortNames : componentViewPortNamesList)
      {
        List<ComponentViewPortName> splitterPortIndices = componentViewPortNames.getSplitterPortIndices();
        for (ComponentViewPortName splitterPortIndex : splitterPortIndices)
        {
          ComponentViewPortNames existing = totalPortWireMap.put(splitterPortIndex, componentViewPortNames);
          if (existing != null)
          {
            throw new SimulatorException("A port connection already existed for splitter port index.");
          }
        }
      }
    }
    return totalPortWireMap;
  }

  private static void processLocalMultiSimulationConnections(CircuitInstanceView startingCircuitInstanceView,
                                                             ConnectionView inputConnectionView,
                                                             List<LocalMultiSimulationConnectionNet> connectionNets,
                                                             List<SplitterProcessStackItem> splitterViewStack,
                                                             Set<ConnectionView> processedSplitterViewConnections,
                                                             Set<SubcircuitPinView> processedSubcircuitPinViews)
  {
    LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet = new LocalMultiSimulationConnectionNet();
    connectionNets.add(localMultiSimulationConnectionNet);

    List<LocalConnectionToProcess> localConnectionsToProcess = new ArrayList<>();
    localConnectionsToProcess.add(new LocalConnectionToProcess(startingCircuitInstanceView, inputConnectionView));

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
                                              List<SplitterProcessStackItem> splitterViewStack,
                                              Set<ConnectionView> processedSplitterViewConnections,
                                              Set<SubcircuitPinView> processedSubcircuitPinViews)
  {
    LocalConnectionNet localConnectionNet = new LocalConnectionNet(localConnectionToProcess.circuitInstanceView);
    localMultiSimulationConnectionNet.add(localConnectionNet);
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

      localConnectionsToProcess.add(new LocalConnectionToProcess(localConnectionToProcess.circuitInstanceView, pinConnection));
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
          ConnectionView subcircuitInstanceConnection = subcircuitPinView.getConnection();
          localConnectionsToProcess.add(new LocalConnectionToProcess(subcircuitInstanceView,
                                                                     subcircuitInstanceConnection));
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

  private static List<SplitterProcessStackItem> createSplitterViewComponentConnections(LocalConnectionNet connectionNet,
                                                                                       Set<ConnectionView> processedSplitterViewConnections)
  {
    List<SplitterProcessStackItem> splitterComponentsConnections = new ArrayList<>();
    List<ComponentConnection<SplitterView>> splitterViews = connectionNet.getSplitterViews();

    updatedProcessedSplitterViewConnections(processedSplitterViewConnections, splitterViews);

    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      List<SplitterProcessStackItem> splitterViewConnectionList = createSplitterViewConnections(processedSplitterViewConnections,
                                                                                                splitterViewConnection.component,
                                                                                                connectionNet.circuitInstanceView);
      splitterComponentsConnections.addAll(splitterViewConnectionList);
    }

    return splitterComponentsConnections;
  }

  private static List<SplitterProcessStackItem> createSplitterViewConnections(Set<ConnectionView> processedSplitterViewConnections,
                                                                              SplitterView splitterView,
                                                                              CircuitInstanceView circuitInstanceView)
  {
    List<SplitterProcessStackItem> splitterComponentsConnections = new ArrayList<>();

    List<PortView> portViews = splitterView.getPortViews();
    for (PortView portView : portViews)
    {
      ConnectionView portConnectionView = portView.getConnection();
      if (!processedSplitterViewConnections.contains(portConnectionView))
      {
        splitterComponentsConnections.add(new SplitterProcessStackItem(splitterView,
                                                                       circuitInstanceView,
                                                                       portConnectionView));
      }
    }
    return splitterComponentsConnections;
  }

  private static void updatedProcessedSplitterViewConnections(Set<ConnectionView> processedSplitterViewConnections,
                                                              List<ComponentConnection<SplitterView>> splitterViews)
  {
    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      processedSplitterViewConnections.add(splitterViewConnection.connection);
    }
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

