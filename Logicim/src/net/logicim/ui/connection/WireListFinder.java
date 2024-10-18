package net.logicim.ui.connection;

import net.common.SimulatorException;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.circuit.CircuitInstanceViewPaths;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitPinView;

import java.util.*;

public abstract class WireListFinder
{
  public static WireList findTraceConnections(CircuitInstanceView circuitInstanceView,
                                              ConnectionView inputConnectionView,
                                              CircuitInstanceViewPaths paths)
  {
    if (inputConnectionView == null)
    {
      throw new SimulatorException("Input connection may not be null.");
    }

    List<SplitterViewProcessStackItem> splitterViewStack = new ArrayList<>();
    CircuitInstanceViewPath firstCircuitInstancePath = paths.getFirst(circuitInstanceView);
    splitterViewStack.add(new SplitterViewProcessStackItem(null, firstCircuitInstancePath, inputConnectionView));

    Set<ConnectionView> processedSplitterViewConnections = new HashSet<>();
    Set<SubcircuitPinView> processedSubcircuitPinViews = new HashSet<>();
    List<LocalMultiSimulationConnectionNet> connectionNets = new ArrayList<>();

    while (splitterViewStack.size() > 0)
    {
      SplitterViewProcessStackItem splitterViewConnection = splitterViewStack.remove(0);
      ConnectionView connectionView = splitterViewConnection.connection;
      if (!processedSplitterViewConnections.contains(connectionView))
      {
        LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet = processLocalMultiSimulationConnections(splitterViewConnection.path,
                                                                                                                     connectionView,
                                                                                                                     splitterViewStack,
                                                                                                                     processedSplitterViewConnections,
                                                                                                                     processedSubcircuitPinViews,
                                                                                                                     paths);
        connectionNets.add(localMultiSimulationConnectionNet);
      }
    }

    for (LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet : connectionNets)
    {
      localMultiSimulationConnectionNet.process();
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
      List<ComponentViewPortNames> componentViewPortNamesList = connectionNet.getComponentViewPortNamesList();
      if (!componentViewPortNamesList.isEmpty())
      {
        List<FullWire> fullWires = createFullWires(totalPortWireMap, processedComponentViewPortNames, componentViewPortNamesList);
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

      SplitterView splitterView = (SplitterView) componentViewPortName.getComponentView();
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
      if (key.isComponent(splitterView) &&
          key.isPortName(oppositeSplitterPort))
      {
        oppositeComponentViewPortNames = entry.getValue();
      }
    }
    return oppositeComponentViewPortNames;
  }

  private static Map<ComponentViewPortName, ComponentViewPortNames> createSplitterPortMap(List<LocalMultiSimulationConnectionNet> connectionNets)
  {
    Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap = new HashMap<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      List<ComponentViewPortNames> componentViewPortNamesList = connectionNet.getComponentViewPortNamesList();
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

  private static LocalMultiSimulationConnectionNet processLocalMultiSimulationConnections(CircuitInstanceViewPath path,
                                                                                          ConnectionView inputConnectionView,
                                                                                          List<SplitterViewProcessStackItem> splitterViewStack,
                                                                                          Set<ConnectionView> processedSplitterViewConnections,
                                                                                          Set<SubcircuitPinView> processedSubcircuitPinViews,
                                                                                          CircuitInstanceViewPaths paths)
  {
    LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet = new LocalMultiSimulationConnectionNet();

    List<ConnectionViewProcessStackItem> localConnectionsToProcess = new ArrayList<>();
    localConnectionsToProcess.add(new ConnectionViewProcessStackItem(path, inputConnectionView));

    while (localConnectionsToProcess.size() > 0)
    {
      ConnectionViewProcessStackItem connectionViewProcessStackItem = localConnectionsToProcess.remove(0);
      processLocalConnections(connectionViewProcessStackItem,
                              localConnectionsToProcess,
                              localMultiSimulationConnectionNet,
                              splitterViewStack,
                              processedSplitterViewConnections,
                              processedSubcircuitPinViews,
                              paths);
    }
    return localMultiSimulationConnectionNet;
  }

  private static void processLocalConnections(ConnectionViewProcessStackItem connectionViewProcessStackItem,
                                              List<ConnectionViewProcessStackItem> localConnectionsToProcess,
                                              LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet,
                                              List<SplitterViewProcessStackItem> splitterViewStack,
                                              Set<ConnectionView> processedSplitterViewConnections,
                                              Set<SubcircuitPinView> processedSubcircuitPinViews,
                                              CircuitInstanceViewPaths paths)
  {
    LocalConnectionNet localConnectionNet = new LocalConnectionNet(connectionViewProcessStackItem.circuitInstanceViewPath);
    localMultiSimulationConnectionNet.add(localConnectionNet);
    localConnectionNet.process(connectionViewProcessStackItem.inputConnectionView);

    splitterViewStack.addAll(createSplitterViewComponentConnections(localConnectionNet, processedSplitterViewConnections));

    for (ComponentConnection<SubcircuitInstanceView> subcircuitInstanceViewConnection : localConnectionNet.subcircuitInstanceViews)
    {
      SubcircuitInstanceView subcircuitInstanceView = subcircuitInstanceViewConnection.component;

      SubcircuitPinView subcircuitPinView = subcircuitInstanceView.getSubcircuitPinView(subcircuitInstanceViewConnection.connection);
      if (subcircuitPinView == null)
      {
        throw new SimulatorException("Subcircuit pin view is [null] for subcircuit instance view [%s] connection [%s].",
                                     subcircuitInstanceView.getDescription(),
                                     subcircuitInstanceViewConnection.connection.toString());
      }
      processedSubcircuitPinViews.add(subcircuitPinView);

      ConnectionView pinConnection = getPinConnectionView(subcircuitInstanceView, subcircuitInstanceViewConnection.connection);

      CircuitInstanceViewPath newPath = paths.getPath(connectionViewProcessStackItem.circuitInstanceViewPath, subcircuitInstanceView);
      localConnectionsToProcess.add(new ConnectionViewProcessStackItem(newPath, pinConnection));
    }

    for (ComponentConnection<PinView> pinViewConnection : localConnectionNet.pinViews)
    {
      PinView pinView = pinViewConnection.component;
      List<SubcircuitPinView> subcircuitPinViews = pinView.getSubcircuitPinViews();
      for (SubcircuitPinView subcircuitPinView : subcircuitPinViews)
      {
        //I'm not sure this check is good enough for partial wires and splitter views.
        if (!processedSubcircuitPinViews.contains(subcircuitPinView))
        {
          processedSubcircuitPinViews.add(subcircuitPinView);

          SubcircuitInstanceView subcircuitInstanceView = subcircuitPinView.getSubcircuitInstanceView();
          ConnectionView subcircuitInstanceConnection = subcircuitPinView.getConnection();
          CircuitInstanceViewPath newPath = paths.getPathExceptLast(connectionViewProcessStackItem.circuitInstanceViewPath, subcircuitInstanceView);
          localConnectionsToProcess.add(new ConnectionViewProcessStackItem(newPath, subcircuitInstanceConnection));
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

  private static List<SplitterViewProcessStackItem> createSplitterViewComponentConnections(LocalConnectionNet connectionNet,
                                                                                           Set<ConnectionView> processedSplitterViewConnections)
  {
    List<SplitterViewProcessStackItem> splitterComponentsConnections = new ArrayList<>();
    List<ComponentConnection<SplitterView>> splitterViews = connectionNet.getSplitterViews();

    updatedProcessedSplitterViewConnections(processedSplitterViewConnections, splitterViews);

    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      List<SplitterViewProcessStackItem> splitterViewConnectionList = createSplitterViewConnections(processedSplitterViewConnections,
                                                                                                    splitterViewConnection.component,
                                                                                                    connectionNet.path);
      splitterComponentsConnections.addAll(splitterViewConnectionList);
    }

    return splitterComponentsConnections;
  }

  private static List<SplitterViewProcessStackItem> createSplitterViewConnections(Set<ConnectionView> processedSplitterViewConnections,
                                                                                  SplitterView splitterView,
                                                                                  CircuitInstanceViewPath path)
  {
    List<SplitterViewProcessStackItem> splitterComponentsConnections = new ArrayList<>();

    List<PortView> portViews = splitterView.getPortViews();
    for (PortView portView : portViews)
    {
      ConnectionView portConnectionView = portView.getConnection();
      if (!processedSplitterViewConnections.contains(portConnectionView))
      {
        splitterComponentsConnections.add(new SplitterViewProcessStackItem(splitterView,
                                                                           path,
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
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (LocalMultiSimulationConnectionNet connectionNet : connectionNets)
    {
      Map<CircuitInstanceViewPath, Set<ConnectionView>> iDontKnowWhatImDoing = connectionNet.getConnectionViews();

      if (iDontKnowWhatImDoing.size() > 1)
      {
        int xxx = 0;
      }

      for (Set<ConnectionView> value : iDontKnowWhatImDoing.values())
      {
        connectionViews.addAll(value);
      }
    }
    return new ArrayList<>(connectionViews);
  }
}

