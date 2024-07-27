package net.logicim.data.circuit;

import net.logicim.data.common.ReflectiveData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.wire.TraceData;

import java.util.List;

public class SubcircuitData
    extends ReflectiveData
{
  public List<StaticData<?>> statics;
  public List<SubcircuitInstanceData> subcircuitInstances;
  public List<TraceData> traces;

  public SubcircuitData()
  {
  }

  public SubcircuitData(List<StaticData<?>> statics,
                        List<SubcircuitInstanceData> subcircuitInstances,
                        List<TraceData> traces)
  {
    this.statics = statics;
    this.subcircuitInstances = subcircuitInstances;
    this.traces = traces;
  }
}

