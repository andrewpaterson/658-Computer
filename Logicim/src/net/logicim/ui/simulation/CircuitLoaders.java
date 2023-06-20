package net.logicim.ui.simulation;

import net.logicim.data.integratedcircuit.common.BoundingBoxData;
import net.logicim.data.simulation.SimulationLoader;
import net.logicim.data.subciruit.SubcircuitInstanceLoader;
import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitInstanceSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import net.logicim.ui.simulation.subcircuit.SubcircuitTopSimulation;

public class CircuitLoaders
{
  public TraceLoader traceLoader;
  public SimulationLoader simulationLoader;
  public SubcircuitInstanceLoader subcircuitLoader;

  public CircuitLoaders()
  {
    traceLoader = new TraceLoader();
    simulationLoader = new SimulationLoader();
    subcircuitLoader = new SubcircuitInstanceLoader();
  }

  public CircuitSimulation createCircuitSimulation(String name, long circuitSimulationId)
  {
    return simulationLoader.create(name, circuitSimulationId);
  }

  public CircuitSimulation getCircuitSimulation(long circuitSimulationId)
  {
    return simulationLoader.getCircuitSimulation(circuitSimulationId);
  }

  public SubcircuitTopSimulation createSubcircuitTopSimulation(CircuitSimulation circuitSimulation,
                                                               SubcircuitEditor subcircuitEditor,
                                                               long subcircuitSimulationId)
  {
    return simulationLoader.create(circuitSimulation, subcircuitEditor, subcircuitSimulationId);
  }

  public SubcircuitInstanceSimulation createSubcircuitInstanceSimulation(CircuitSimulation circuitSimulation,
                                                                         SubcircuitInstance subcircuitInstance,
                                                                         long subcircuitSimulationId)
  {
    return simulationLoader.create(circuitSimulation, subcircuitInstance, subcircuitSimulationId);
  }

  public SubcircuitInstance getSubcircuitInstance(long subcircuitInstanceId)
  {
    return subcircuitLoader.get(subcircuitInstanceId);
  }

  public SubcircuitSimulation getSubcircuitSimulation(long subcircuitSimulationId)
  {
    return simulationLoader.getSubcircuitSimulation(subcircuitSimulationId);
  }

  public TraceLoader getTraceLoader()
  {
    return traceLoader;
  }
}

