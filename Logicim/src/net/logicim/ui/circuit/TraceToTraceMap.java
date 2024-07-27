package net.logicim.ui.circuit;

import net.logicim.domain.common.wire.Trace;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TraceToTraceMap
{
  protected Map<Trace, Trace> traces;

  public TraceToTraceMap()
  {
    traces = new LinkedHashMap<>();
  }

  public Trace makeTrace(Trace trace)
  {
    Trace result = traces.get(trace);
    if (result == null)
    {
      result = new Trace();
      traces.put(trace, result);
    }
    return result;
  }

  public List<Trace> makeTraces(List<Trace> traces)
  {
    ArrayList<Trace> result = new ArrayList<>(traces.size());
    for (Trace trace : traces)
    {
      result.add(makeTrace(trace));
    }
    return result;
  }
}

