package net.logicim.data.simulation;

import net.logicim.data.common.ReflectiveData;

public abstract class SubcircuitSimulationData
    extends ReflectiveData
{
  public long subcircuitSimulation;
  public long subcircuitEditor;
  public long circuitSimulation;

  public SubcircuitSimulationData()
  {
  }

  public SubcircuitSimulationData(long subcircuitSimulation,
                                  long subcircuitEditor,
                                  long circuitSimulation)
  {
    this.subcircuitSimulation = subcircuitSimulation;
    this.subcircuitEditor = subcircuitEditor;
    this.circuitSimulation = circuitSimulation;
  }
}

