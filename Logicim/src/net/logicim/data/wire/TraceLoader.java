package net.logicim.data.wire;

import net.logicim.domain.common.wire.Trace;

import java.util.HashMap;
import java.util.Map;

public class TraceLoader
{
  protected Map<Long, Trace> tracesById;

  public TraceLoader()
  {
    Trace.resetNextId();
    tracesById = new HashMap<>();
  }

  public Trace create(long id)
  {
    if (id > 0)
    {
      Trace trace = tracesById.get(id);
      if (trace == null)
      {
        trace = new Trace(id);
        tracesById.put(id, trace);
      }
      return trace;
    }
    else
    {
      return null;
    }
  }
}

