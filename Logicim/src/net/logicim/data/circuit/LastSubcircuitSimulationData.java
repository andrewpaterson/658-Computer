package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;

public class LastSubcircuitSimulationData
    extends ReflectiveData
{
  public long subcircuitEditor;
  public long subcircuitSimulation;

  public LastSubcircuitSimulationData()
  {
  }

  public LastSubcircuitSimulationData(long subcircuitEditor, long subcircuitSimulation)
  {
    this.subcircuitEditor = subcircuitEditor;
    this.subcircuitSimulation = subcircuitSimulation;
  }
}

