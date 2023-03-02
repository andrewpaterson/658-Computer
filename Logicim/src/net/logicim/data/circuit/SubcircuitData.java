package net.logicim.data.circuit;

import net.logicim.data.ReflectiveData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;

import java.util.List;

public class SubcircuitData
    extends ReflectiveData
{
  public List<StaticData<?>> components;
  public List<TraceData> traces;

  public SubcircuitData()
  {
  }

  public SubcircuitData(List<StaticData<?>> components, List<TraceData> traces)
  {
    this.components = components;
    this.traces = traces;
  }
}
