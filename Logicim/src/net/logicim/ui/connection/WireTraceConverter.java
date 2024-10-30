package net.logicim.ui.connection;

import net.common.SimulatorException;
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
  protected CircuitInstanceViewPath startingCircuitInstanceViewPath;
  protected CircuitSimulation circuitSimulation;

  public WireTraceConverter(WireList wireList,
                            SubcircuitSimulation startingSubcircuitSimulation,
                            CircuitInstanceViewPaths paths)
  {
    this.paths = paths;
    this.wireList = wireList;
    this.startingCircuitInstanceViewPath = paths.getSubcircuitSimulationPaths().get(startingSubcircuitSimulation);
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
    Simulation simulation = circuitSimulation.getSimulation();

    List<FullWire> fullWires = wireList.getFullWires();
    for (FullWire fullWire : fullWires)
    {
      Trace trace = new Trace();
      Set<ComponentViewPortNames> localWires = fullWire.getLocalWires(circuitSimulation);
      for (ComponentViewPortNames localWire : localWires)
      {
        List<ComponentViewPortName> connectedPortIndices = localWire.getConnectedPortIndices();
        for (ComponentViewPortName connectedPortIndex : connectedPortIndices)
        {
          ComponentView<?> componentView = connectedPortIndex.getComponentView();
          String portName = connectedPortIndex.getPortName();
          CircuitInstanceViewPath path = connectedPortIndex.getPath();

          SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation(path);

          Port port = componentView.getPort(subcircuitSimulation, portName);
          if (port == null)
          {
            throw new SimulatorException("Could not find Port [%s] on %s [%s] for simulation [%s].",
                                         portName,
                                         componentView.getClass().getSimpleName(),
                                         componentView.getDescription(),
                                         subcircuitSimulation.getDescription());
          }
          port.disconnect(simulation);
          port.connect(trace);
        }

        LocalMultiSimulationConnectionNet multiSimulationConnectionNet = localWire.getMultiSimulationConnectionNet();
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
    for (PartialWire partialWire : wireList.getPartialWires(circuitSimulation))
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
          wireView.destroyComponent(subcircuitSimulation);
        }
      }
    }

    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets(circuitSimulation))
    {
      for (Map.Entry<CircuitInstanceViewPath, List<WireConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        CircuitInstanceViewPath path = entry.getKey();
        List<WireConnection> wireConnections = entry.getValue();
        for (WireConnection connectedWire : wireConnections)
        {
          WireView wireView = connectedWire.wireView;
          SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation(path);
          wireView.destroyComponent(subcircuitSimulation);
        }
      }
    }
  }

  private SubcircuitSimulation getSubcircuitSimulation(CircuitInstanceViewPath path)
  {
    return path.getSubcircuitSimulation(circuitSimulation);
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

