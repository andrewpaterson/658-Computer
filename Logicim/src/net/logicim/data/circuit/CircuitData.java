package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;
import net.logicim.data.simulation.CircuitSimulationData;
import net.logicim.data.simulation.SubcircuitSimulationData;
import net.logicim.data.subciruit.ViewPathData;

import java.util.List;

public class CircuitData
    extends ReflectiveData
{
  public List<SubcircuitEditorData> subcircuits;
  public List<CircuitSimulationData> circuitSimulations;
  public List<SubcircuitSimulationData> subcircuitSimulations;
  public List<ViewPathData> viewPathDatas;
  public List<LastSubcircuitSimulationData> lastSubcircuitSimulationDatas;
  public long currentSubcircuit;
  public long currentViewPath;
  public long currentCircuitSimulation;

  public CircuitData()
  {
  }

  public CircuitData(List<SubcircuitEditorData> subcircuits,
                     List<CircuitSimulationData> circuitSimulations,
                     List<SubcircuitSimulationData> subcircuitSimulations,
                     List<ViewPathData> viewPathDatas,
                     List<LastSubcircuitSimulationData> lastSubcircuitSimulationDatas,
                     long currentSubcircuit,
                     long currentViewPath,
                     long currentCircuitSimulation)
  {
    this.subcircuits = subcircuits;
    this.circuitSimulations = circuitSimulations;
    this.subcircuitSimulations = subcircuitSimulations;
    this.viewPathDatas = viewPathDatas;
    this.lastSubcircuitSimulationDatas = lastSubcircuitSimulationDatas;
    this.currentSubcircuit = currentSubcircuit;
    this.currentViewPath = currentViewPath;
    this.currentCircuitSimulation = currentCircuitSimulation;
  }
}

