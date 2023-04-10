package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;

import java.util.List;

public class SubcircuitData
    extends ReflectiveData
{
  public List<StaticData<?>> components;
  public List<TraceData> traces;
  public String typeName;
  public long id;

  public SubcircuitData()
  {
  }

  public SubcircuitData(List<StaticData<?>> components,
                        List<TraceData> traces,
                        String typeName,
                        long id)
  {
    this.components = components;
    this.traces = traces;
    this.typeName = typeName;
    this.id = id;
  }
}

