package net.logicim.ui.clipboard;

import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.wire.TraceData;

import java.util.List;

public class ClipboardData
{
  protected List<StaticData<?>> components;
  protected List<TraceData> traces;

  public ClipboardData(List<StaticData<?>> components, List<TraceData> traces)
  {
    this.components = components;
    this.traces = traces;
  }

  public List<StaticData<?>> getComponents()
  {
    return components;
  }

  public List<TraceData> getTraces()
  {
    return traces;
  }
}

