package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;

public class SubcircuitEditorData
    extends ReflectiveData
{
  public long id;
  public SubcircuitData subcircuit;
  public String typeName;

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

