package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;

public class LastSubcircuitSimulationData
    extends ReflectiveData
{
  public long subcircuitEditor;
  public long circuitSimulation;
  public long viewPath;

  public LastSubcircuitSimulationData()
  {
  }

  public LastSubcircuitSimulationData(long subcircuitEditor, long circuitSimulation, long viewPath)
  {
    this.subcircuitEditor = subcircuitEditor;
    this.circuitSimulation = circuitSimulation;
    this.viewPath = viewPath;
  }
}

