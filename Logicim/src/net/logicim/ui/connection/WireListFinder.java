package net.logicim.ui.connection;

import net.common.SimulatorException;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPaths;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.passive.splitter.SplitterView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitPinView;

import java.util.*;

public class WireListFinder
{
  protected ViewPaths paths;
  protected List<PathConnectionView> pathConnectionViewStack;
  protected Set<SubcircuitPinView> processedSubcircuitPinViews;
  protected Set<ConnectionView> processedSplitterViewConnections;

  public WireListFinder(ViewPath viewPath,
                        ConnectionView inputConnectionView,
                        ViewPaths paths)
  {
    if (inputConnectionView == null)
    {
      throw new SimulatorException("Input connection may not be null.");
    }

    this.paths = paths;
    this.pathConnectionViewStack = new ArrayList<>();
    CircuitInstanceView circuitInstanceView = viewPath.getLast();
    SubcircuitView subcircuitView = circuitInstanceView.getInstanceSubcircuitView();
    PathConnectionView pathConnectionView = subcircuitView.getPathConnection(viewPath, inputConnectionView);
    this.pathConnectionViewStack.add(pathConnectionView);
    this.processedSubcircuitPinViews = new LinkedHashSet<>();
    this.processedSplitterViewConnections = new LinkedHashSet<>();
  }

  public List<LocalMultiSimulationConnectionNet> createConnectionNets()
  {
    List<LocalMultiSimulationConnectionNet> connectionNets = new ArrayList<>();

    while (pathConnectionViewStack.size() > 0)
    {
      PathConnectionView pathConnectionView = pathConnectionViewStack.remove(0);
      ConnectionView connectionView = pathConnectionView.connection;
      if (!processedSplitterViewConnections.contains(connectionView))
      {
        LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet = processLocalMultiSimulationConnections(pathConnectionView.viewPath, connectionView);
        connectionNets.add(localMultiSimulationConnectionNet);
      }
    }

    for (LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet : connectionNets)
    {
      localMultiSimulationConnectionNet.process();
    }
    return connectionNets;
  }

  public WireList createWireList(List<LocalMultiSimulationConnectionNet> connectionNets)
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
        Map<ViewPath, List<WireViewPathConnection>> connectedWires = connectionNet.getConnectedWires();
        if (connectedWires.size() > 0)
        {
          wireList.add(new PartialWire(connectedWires));
        }
      }
    }
    return wireList;
  }

  private List<FullWire> createFullWires(Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap,
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

  private FullWire createFullWire(Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap,
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

  private ComponentViewPortNames getOppositePortConnection(Map<ComponentViewPortName, ComponentViewPortNames> totalPortWireMap, SplitterView splitterView, String oppositeSplitterPort)
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

  private Map<ComponentViewPortName, ComponentViewPortNames> createSplitterPortMap(List<LocalMultiSimulationConnectionNet> connectionNets)
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

  private LocalMultiSimulationConnectionNet processLocalMultiSimulationConnections(ViewPath viewPath,
                                                                                   ConnectionView inputConnectionView)
  {
    LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet = new LocalMultiSimulationConnectionNet();

    List<ConnectionViewProcessStackItem> localConnectionsToProcess = new ArrayList<>();
    localConnectionsToProcess.add(new ConnectionViewProcessStackItem(viewPath, inputConnectionView));

    while (localConnectionsToProcess.size() > 0)
    {
      ConnectionViewProcessStackItem connectionViewProcessStackItem = localConnectionsToProcess.remove(0);
      processLocalConnections(connectionViewProcessStackItem, localConnectionsToProcess, localMultiSimulationConnectionNet);
    }

    return localMultiSimulationConnectionNet;
  }

  private void processLocalConnections(ConnectionViewProcessStackItem connectionViewProcessStackItem,
                                       List<ConnectionViewProcessStackItem> localConnectionsToProcess,
                                       LocalMultiSimulationConnectionNet localMultiSimulationConnectionNet)
  {
    LocalConnectionNet localConnectionNet = new LocalConnectionNet(connectionViewProcessStackItem.viewPath, connectionViewProcessStackItem.inputConnectionView);
    localMultiSimulationConnectionNet.add(localConnectionNet);

    List<PathConnectionView> splitterViewComponentConnections = createSplitterViewStackItemsForLocalConnectionNet(localConnectionNet);
    pathConnectionViewStack.addAll(splitterViewComponentConnections);

    List<ConnectionViewProcessStackItem> subcircuitInstanceViewItemsToProcess = createConnectionViewStackItemsForSubcircuitInstanceViews(localConnectionNet.subcircuitInstanceViews, connectionViewProcessStackItem.viewPath);
    localConnectionsToProcess.addAll(subcircuitInstanceViewItemsToProcess);

    List<ConnectionViewProcessStackItem> pinViewItemsToProcess = createConnectionViewStackItemsForPinViews(connectionViewProcessStackItem, localConnectionNet.pinViews);
    localConnectionsToProcess.addAll(pinViewItemsToProcess);
  }

  private List<ConnectionViewProcessStackItem> createConnectionViewStackItemsForSubcircuitInstanceViews(List<ComponentConnection<SubcircuitInstanceView>> subcircuitInstanceViews, ViewPath viewPath)
  {
    List<ConnectionViewProcessStackItem> localConnectionsToProcess = new ArrayList<>();
    for (ComponentConnection<SubcircuitInstanceView> subcircuitInstanceViewConnection : subcircuitInstanceViews)
    {
      SubcircuitInstanceView subcircuitInstanceView = subcircuitInstanceViewConnection.componentView;

      SubcircuitPinView subcircuitPinView = subcircuitInstanceView.getSubcircuitPinView(subcircuitInstanceViewConnection.connectionView);
      if (subcircuitPinView == null)
      {
        throw new SimulatorException("Subcircuit pin view is [null] for subcircuit instance view [%s] connection [%s].",
                                     subcircuitInstanceView.getDescription(),
                                     subcircuitInstanceViewConnection.connectionView.toString());
      }
      processedSubcircuitPinViews.add(subcircuitPinView);

      ConnectionView pinConnection = getPinConnectionView(subcircuitInstanceView, subcircuitInstanceViewConnection.connectionView);

      ViewPath newPath = paths.getViewPath(viewPath, subcircuitInstanceView);
      localConnectionsToProcess.add(new ConnectionViewProcessStackItem(newPath, pinConnection));
    }
    return localConnectionsToProcess;
  }

  private List<ConnectionViewProcessStackItem> createConnectionViewStackItemsForPinViews(ConnectionViewProcessStackItem connectionViewProcessStackItem, List<ComponentConnection<PinView>> pinViews)
  {
    ViewPath viewPath = connectionViewProcessStackItem.viewPath;

    List<ConnectionViewProcessStackItem> localConnectionsToProcess = new ArrayList<>();
    for (ComponentConnection<PinView> pinViewConnection : pinViews)
    {
      PinView pinView = pinViewConnection.componentView;
      List<SubcircuitPinView> subcircuitPinViews = pinView.getSubcircuitPinViews();
      for (SubcircuitPinView subcircuitPinView : subcircuitPinViews)
      {
        CircuitInstanceView pathLast = viewPath.getLast();
        if (subcircuitPinView.getSubcircuitInstanceView() == pathLast)
        {
          //I'm not sure this check is good enough for partial wires and splitter views.
          if (!processedSubcircuitPinViews.contains(subcircuitPinView))
          {
            processedSubcircuitPinViews.add(subcircuitPinView);

            ConnectionView subcircuitInstanceConnection = subcircuitPinView.getConnection();
            ViewPath newPath = paths.getPathExceptLast(viewPath);
            localConnectionsToProcess.add(new ConnectionViewProcessStackItem(newPath, subcircuitInstanceConnection));
          }
        }
      }
    }
    return localConnectionsToProcess;
  }

  private ConnectionView getPinConnectionView(SubcircuitInstanceView subcircuitInstanceView, ConnectionView connectionView)
  {
    SubcircuitPinView subcircuitPinView = subcircuitInstanceView.getSubcircuitPinView(connectionView);
    PinView pinView = subcircuitPinView.getPinView();
    PortView portView = pinView.getPortView();
    return portView.getConnection();
  }

  private List<PathConnectionView> createSplitterViewStackItemsForLocalConnectionNet(LocalConnectionNet connectionNet)
  {
    List<PathConnectionView> splitterComponentsConnections = new ArrayList<>();
    List<ComponentConnection<SplitterView>> splitterViews = connectionNet.getSplitterViews();

    updatedProcessedSplitterViewConnections(splitterViews);

    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      List<PathConnectionView> splitterViewConnectionList = createSplitterViewConnections(splitterViewConnection.componentView, connectionNet.viewPath);
      splitterComponentsConnections.addAll(splitterViewConnectionList);
    }

    return splitterComponentsConnections;
  }

  private List<PathConnectionView> createSplitterViewConnections(SplitterView splitterView,
                                                                 ViewPath viewPath)
  {
    List<PathConnectionView> splitterComponentsConnections = new ArrayList<>();

    List<PortView> portViews = splitterView.getPortViews();
    for (PortView portView : portViews)
    {
      ConnectionView portConnectionView = portView.getConnection();
      if (!processedSplitterViewConnections.contains(portConnectionView))
      {
        SubcircuitView subcircuitView = viewPath.getLast().getInstanceSubcircuitView();
        PathConnectionView pathConnectionView = subcircuitView.getPathConnection(viewPath, portConnectionView);
        splitterComponentsConnections.add(pathConnectionView);
      }
    }
    return splitterComponentsConnections;
  }

  private void updatedProcessedSplitterViewConnections(List<ComponentConnection<SplitterView>> splitterViews)
  {
    for (ComponentConnection<SplitterView> splitterViewConnection : splitterViews)
    {
      processedSplitterViewConnections.add(splitterViewConnection.connectionView);
    }
  }
}

