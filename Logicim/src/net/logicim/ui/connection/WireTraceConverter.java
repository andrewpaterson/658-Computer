package net.logicim.ui.connection;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.CircuitInstanceViewPath;
import net.logicim.ui.circuit.CircuitInstanceViewPaths;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.wire.WireView;

import java.util.*;

public class WireTraceConverter
{
  protected Map<LocalMultiSimulationConnectionNet, List<Trace>> localMultiSimulationConnectionNetMap;
  protected CircuitInstanceViewPaths paths;
  protected WireList wireList;
  protected SubcircuitSimulation startingSubcircuitSimulation;
  protected CircuitInstanceViewPath startingCircuitInstanceViewPath;
  protected CircuitSimulation circuitSimulation;

  public WireTraceConverter(WireList wireList,
                            SubcircuitSimulation startingSubcircuitSimulation,
                            CircuitInstanceViewPaths paths)
  {
    this.paths = paths;
    this.wireList = wireList;
    this.startingSubcircuitSimulation = startingSubcircuitSimulation;
    this.startingCircuitInstanceViewPath = paths.getSubcircuitSimulations().get(startingSubcircuitSimulation);
    this.circuitSimulation = startingSubcircuitSimulation.getCircuitSimulation();

    this.localMultiSimulationConnectionNetMap = new HashMap<>();
  }

  public void process()
  {
    createTracesAndConnectPorts();

    disconnectWireViews();
    connectWireViews();
  }

  private void createTracesAndConnectPorts()
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

  private void connectWireViews()
  {
    for (Map.Entry<LocalMultiSimulationConnectionNet, List<Trace>> connectionNetEntry : localMultiSimulationConnectionNetMap.entrySet())
    {
      LocalMultiSimulationConnectionNet connectionNet = connectionNetEntry.getKey();
      List<Trace> traces = connectionNetEntry.getValue();

      Set<WireView> processedWireViews = new HashSet<>();
      Map<CircuitInstanceViewPath, List<WireConnection>> connectedWires = connectionNet.getConnectedWires();
      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> wireEntry : connectedWires.entrySet())
      {
        List<WireConnection> wireConnections = wireEntry.getValue();
        CircuitInstanceViewPath path = wireEntry.getKey();
        SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation(path);
        for (WireConnection wireConnection : wireConnections)
        {
          WireView wireView = wireConnection.getWireView();
          if (!processedWireViews.contains(wireView))
          {
            wireView.connectTraces(subcircuitSimulation, traces);
            processedWireViews.add(wireView);
          }
        }
      }
    }
  }

  protected void disconnectWireViews()
  {
    for (PartialWire partialWire : wireList.getPartialWires())
    {
      Map<CircuitInstanceViewPath, List<WireConnection>> connectedWires = partialWire.connectedWires;
      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> wireEntry : connectedWires.entrySet())
      {
        CircuitInstanceViewPath path = wireEntry.getKey();
        List<WireConnection> wireConnections = wireEntry.getValue();
        for (WireConnection wireConnection : wireConnections)
        {
          WireView wireView = wireConnection.getWireView();

          SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation(path);
          wireView.disconnect(subcircuitSimulation);
        }
      }
    }

    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets())
    {
      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        CircuitInstanceViewPath path = entry.getKey();
        List<WireConnection> wireConnections = entry.getValue();
        for (WireConnection connectedWire : wireConnections)
        {
          WireView wireView = connectedWire.wireView;
          SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation(path);
          wireView.disconnect(subcircuitSimulation);
        }
      }
    }
  }

  private SubcircuitSimulation getSubcircuitSimulation(CircuitInstanceViewPath path)
  {
    return paths.getSubcircuitSimulation(circuitSimulation, path);
  }

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

