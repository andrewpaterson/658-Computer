package net.logicim.data.simulation;

import net.logicim.data.common.ReflectiveData;

public abstract class SubcircuitSimulationData
    extends ReflectiveData
{
  public long viewPath;
  public long subcircuitEditor;
  public long circuitSimulation;
  public long subcircuitSimulation;

  public SubcircuitSimulationData()
  {
  }

  public SubcircuitSimulationData(long viewPath,
                                  long subcircuitEditor,
                                  long circuitSimulation,
                                  long subcircuitSimulation)
  {
    this.viewPath = viewPath;
    this.subcircuitEditor = subcircuitEditor;
    this.circuitSimulation = circuitSimulation;
    this.subcircuitSimulation = subcircuitSimulation;
  }
}

