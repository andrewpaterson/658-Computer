package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.data.simulation.SubcircuitSimulationData;
import net.logicim.data.simulation.SubcircuitTopSimulationData;

import java.util.List;

public class CircuitData
    extends ReflectiveData
{
  public List<SubcircuitEditorData> subcircuits;
  public List<CircuitSimulationData> circuitSimulations;
  public List<SubcircuitSimulationData> subcircuitSimulations;
  public long currentSubcircuit;
  public long currentSimulation;

  public CircuitData()
  {
  }

  public CircuitData(List<SubcircuitEditorData> subcircuits,
                     List<CircuitSimulationData> circuitSimulations,
                     List<SubcircuitSimulationData> subcircuitSimulations,
                     long currentSubcircuit,
                     long currentSimulation)
  {
    this.subcircuits = subcircuits;
    this.circuitSimulations = circuitSimulations;
    this.subcircuitSimulations = subcircuitSimulations;
    this.currentSubcircuit = currentSubcircuit;
    this.currentSimulation = currentSimulation;
  }
}

