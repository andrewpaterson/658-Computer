package net.logicim.ui.connection;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceView;
import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.circuit.CircuitInstanceViewPaths;
import net.logicim.ui.common.integratedcircuit.ComponentView;

import java.util.*;

public class WireTraceConverter
{
  protected Map<LocalMultiSimulationConnectionNet, List<Trace>> localMultiSimulationConnectionNetMap;

  public WireTraceConverter(CircuitInstanceViewPaths circuitInstanceViewPaths, SubcircuitSimulation startingSubcircuitSimulation)
  {
    localMultiSimulationConnectionNetMap = new HashMap<>();

    for (CircuitInstanceViewPath circuitInstanceViewPath : circuitInstanceViewPaths.getPaths())
    {
      List<CircuitInstanceView> path = circuitInstanceViewPath.getPath();
      SubcircuitSimulation subcircuitSimulation =startingSubcircuitSimulation;
      for (CircuitInstanceView circuitInstanceView : path)
      {
        circuitInstanceView.getDescription();
      }
    }
  }

  public void createTracesAndConnectPorts(WireList wireList, SubcircuitSimulation verySuspect)
  {
    createTracesAndConnectPorts(verySuspect, wireList);

//    disconnectWireViews(wireList);
//    connectWireViews(wireList);
  }

  private void createTracesAndConnectPorts(SubcircuitSimulation startingSubcircuitSimulation, WireList wireList)
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

        addTrace(multiSimulationConnectionNet, trace);
      }
    }
  }

//  protected void connectWireViews(WireList wireList)
//  {
//    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets())
//    {
//      Set<WireView> processedWireViews = new HashSet<>();
//      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
//      {
//        CircuitInstanceViewPath path = entry.getKey();
//        List<WireConnection> wireConnections = entry.getValue();
//        for (WireConnection connectedWire : wireConnections)
//        {
//          WireView wireView = connectedWire.wireView;
//          if (!processedWireViews.contains(wireView))
//          {
//            wireView.connectTraces(path, connectionNet.getTraces());
//            processedWireViews.add(wireView);
//          }
//        }
//      }
//    }
//  }
//
//  protected void disconnectWireViews(WireList wireList)
//  {
//    for (PartialWire partialWire : wireList.getPartialWires())
//    {
//      Map<CircuitInstanceViewPath, List<WireConnection>> connectedWires = partialWire.connectedWires;
//      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectedWires.entrySet())
//      {
//        SubcircuitSimulation subcircuitSimulation = entry.getKey();
//        List<WireConnection> wireConnections1 = entry.getValue();
//        for (WireConnection wireConnection : wireConnections1)
//        {
//          WireView wireView = wireConnection.getWireView();
//          wireView.disconnect(subcircuitSimulation);
//        }
//      }
//    }
//
//    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets())
//    {
//      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
//      {
//        CircuitInstanceViewPath subcircuitSimulation = entry.getKey();
//        List<WireConnection> wireConnections = entry.getValue();
//        for (WireConnection connectedWire : wireConnections)
//        {
//          WireView wireView = connectedWire.wireView;
//          wireView.disconnect(subcircuitSimulation);
//        }
//      }
//    }
//  }

  private void addTrace(LocalMultiSimulationConnectionNet multiSimulationConnectionNet, Trace trace)
  {
    List<Trace> traces = localMultiSimulationConnectionNetMap.get(multiSimulationConnectionNet);
    if (traces == null)
    {
      traces = new ArrayList<>();
      localMultiSimulationConnectionNetMap.put(multiSimulationConnectionNet, traces);
    }
    traces.add(trace);
  }
}
