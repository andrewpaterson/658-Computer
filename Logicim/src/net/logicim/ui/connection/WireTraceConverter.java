package net.logicim.ui.connection;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.wire.WireView;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class WireTraceConverter
{
  public static void createTracesAndConnectPorts(WireList wireList, SubcircuitSimulation startingSubcircuitSimulation)
  {
    createTracesAndConnectPorts(startingSubcircuitSimulation, wireList);
    disconnectWireViews(wireList);
    connectWireViews(wireList);
  }

  protected static void connectWireViews(WireList wireList)
  {
    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets())
    {
      Set<WireView> processedWireViews = new HashSet<>();
      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        CircuitInstanceViewPath path = entry.getKey();
        List<WireConnection> wireConnections = entry.getValue();
        for (WireConnection connectedWire : wireConnections)
        {
          WireView wireView = connectedWire.wireView;
          if (!processedWireViews.contains(wireView))
          {
            wireView.connectTraces(path, connectionNet.getTraces());
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
      Map<CircuitInstanceViewPath, List<WireConnection>> connectedWires = partialWire.connectedWires;
      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectedWires.entrySet())
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
      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        CircuitInstanceViewPath subcircuitSimulation = entry.getKey();
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
}
