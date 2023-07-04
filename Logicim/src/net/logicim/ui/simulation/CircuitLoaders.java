package net.logicim.ui.simulation;

import net.logicim.data.simulation.SimulationLoader;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

public class CircuitLoaders
{
  public TraceLoader traceLoader;
  public SimulationLoader simulationLoader;

  public CircuitLoaders()
  {
    traceLoader = new TraceLoader();
    simulationLoader = new SimulationLoader();
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
}

