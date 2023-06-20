package net.logicim.data.simulation;

import net.logicim.data.common.ReflectiveData;

public abstract class SubcircuitSimulationData
    extends ReflectiveData
{
  public long id;
  public long circuitSimulationId;

  public SubcircuitSimulationData()
  {
  }

  public SubcircuitSimulationData(long id)
  {
    this.id = id;
  }
}

