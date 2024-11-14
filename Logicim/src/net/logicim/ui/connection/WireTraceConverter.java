package net.logicim.ui.connection;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.SubcircuitSimulationPaths;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.wire.WireView;

import java.util.*;

public class WireTraceConverter
{
  protected Map<LocalMultiSimulationConnectionNet, List<Trace>> localMultiSimulationConnectionNetMap;
  protected WireList wireList;
  protected ViewPath startingViewPath;
  protected CircuitSimulation circuitSimulation;

  public WireTraceConverter(WireList wireList,
                            SubcircuitSimulation startingSubcircuitSimulation,
                            SubcircuitSimulationPaths subcircuitSimulationPaths)
  {
    this.wireList = wireList;
    this.startingViewPath = subcircuitSimulationPaths.getViewPath(startingSubcircuitSimulation);
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
          ViewPath path = connectedPortIndex.getPath();

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
      Map<ViewPath, List<WireViewPathConnection>> connectedWires = connectionNet.getConnectedWires();
      for (Map.Entry<ViewPath, List<WireViewPathConnection>> wireEntry : connectedWires.entrySet())
      {
        List<WireViewPathConnection> wireViewPathConnections = wireEntry.getValue();
        ViewPath path = wireEntry.getKey();
        SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation(path);
        for (WireViewPathConnection wireViewPathConnection : wireViewPathConnections)
        {
          WireView wireView = wireViewPathConnection.getWireView();
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
      Map<ViewPath, List<WireViewPathConnection>> connectedWires = partialWire.connectedWires;
      for (Map.Entry<ViewPath, List<WireViewPathConnection>> wireEntry : connectedWires.entrySet())
      {
        ViewPath path = wireEntry.getKey();
        List<WireViewPathConnection> wireViewPathConnections = wireEntry.getValue();
        for (WireViewPathConnection wireViewPathConnection : wireViewPathConnections)
        {
          WireView wireView = wireViewPathConnection.getWireView();

          SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation(path);
          wireView.destroyComponent(subcircuitSimulation);
        }
      }
    }

    for (LocalMultiSimulationConnectionNet connectionNet : wireList.getConnectionNets(circuitSimulation))
    {
      for (Map.Entry<ViewPath, List<WireViewPathConnection>> entry : connectionNet.getConnectedWires().entrySet())
      {
        ViewPath path = entry.getKey();
        List<WireViewPathConnection> wireViewPathConnections = entry.getValue();
        for (WireViewPathConnection connectedWire : wireViewPathConnections)
        {
          WireView wireView = connectedWire.wireView;
          SubcircuitSimulation subcircuitSimulation = getSubcircuitSimulation(path);
          wireView.destroyComponent(subcircuitSimulation);
        }
      }
    }
  }

  private SubcircuitSimulation getSubcircuitSimulation(ViewPath path)
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

