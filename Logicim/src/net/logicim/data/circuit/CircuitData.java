package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.data.simulation.SubcircuitSimulationData;

import java.util.List;

public class CircuitData
    extends ReflectiveData
{
  public List<SubcircuitEditorData> subcircuits;
  public List<CircuitSimulationData> circuitSimulations;
  public List<SubcircuitSimulationData> subcircuitSimulations;
  public List<LastSubcircuitSimulationData> lastSubcircuitSimulationDatas;
  public long currentSubcircuit;
  public long currentSubcircuitSimulation;

  public CircuitData()
  {
  }

  public CircuitData(List<SubcircuitEditorData> subcircuits,
                     List<CircuitSimulationData> circuitSimulations,
                     List<SubcircuitSimulationData> subcircuitSimulations,
                     List<LastSubcircuitSimulationData> lastSubcircuitSimulationDatas,
                     long currentSubcircuit,
                     long currentSubcircuitSimulation)
  {
    this.subcircuits = subcircuits;
    this.circuitSimulations = circuitSimulations;
    this.subcircuitSimulations = subcircuitSimulations;
    this.lastSubcircuitSimulationDatas = lastSubcircuitSimulationDatas;
    this.currentSubcircuit = currentSubcircuit;
    this.currentSubcircuitSimulation = currentSubcircuitSimulation;
  }
}

