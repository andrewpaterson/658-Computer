package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;
import net.logicim.data.simulation.SubcircuitSimulationData;

import java.util.List;

public class SubcircuitEditorData
    extends ReflectiveData
{
  public SubcircuitData subcircuit;
  public String typeName;
  public long id;
  public List<SubcircuitSimulationData> subcircuitSimulations;

  public SubcircuitEditorData()
  {
  }

  public SubcircuitEditorData(SubcircuitData subcircuit,
                              String typeName,
                              long id)
  {
    this.subcircuit = subcircuit;
    this.typeName = typeName;
    this.id = id;
  }
}

