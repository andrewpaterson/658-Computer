package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;
import net.logicim.data.simulation.CircuitSimulationData;

import java.util.List;

public class CircuitData
    extends ReflectiveData
{
  public List<SubcircuitData> subcircuits;
  public List<CircuitSimulationData> circuitSimulationDatas;
  public long currentSubcircuit;
  public long currentSimulation;

  public CircuitData()
  {
  }

  public CircuitData(List<SubcircuitData> subcircuits,
                     List<CircuitSimulationData> circuitSimulationDatas,
                     long currentSubcircuit,
                     long currentSimulation)
  {
    this.subcircuits = subcircuits;
    this.circuitSimulationDatas = circuitSimulationDatas;
    this.currentSubcircuit = currentSubcircuit;
    this.currentSimulation = currentSimulation;
  }
}

