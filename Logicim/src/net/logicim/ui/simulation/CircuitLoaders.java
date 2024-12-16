package net.logicim.ui.simulation;

import net.logicim.data.circuit.ViewPathCircuitSimulationLoader;
import net.logicim.data.simulation.SimulationLoader;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;

public class CircuitLoaders
{
  public TraceLoader traceLoader;
  public SimulationLoader simulationLoader;
  public ViewPathCircuitSimulationLoader viewPathLoader;

  public CircuitLoaders()
  {
    traceLoader = new TraceLoader();
    simulationLoader = new SimulationLoader();
    viewPathLoader = new ViewPathCircuitSimulationLoader();
  }

  public CircuitSimulation getCircuitSimulation(long circuitSimulationId)
  {
    return simulationLoader.getCircuitSimulation(circuitSimulationId);
  }

  public SubcircuitSimulation getSubcircuitSimulation(long subcircuitSimulationId)
  {
    return simulationLoader.getSubcircuitSimulation(subcircuitSimulationId);
  }

  public SimulationLoader getSimulationLoader()
  {
    return simulationLoader;
  }

  public TraceLoader getTraceLoader()
  {
    return traceLoader;
  }

  public ViewPathCircuitSimulationLoader getViewPathLoader()
  {
    return viewPathLoader;
  }
}

