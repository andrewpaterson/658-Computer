package net.logicim.data.trace;

import net.logicim.domain.common.trace.TraceNet;

import java.util.HashMap;
import java.util.Map;

public class TraceLoader
{
  protected Map<Long, TraceNet> tracesById;

  public TraceLoader()
  {
    TraceNet.resetNextId();
    tracesById = new HashMap<>();
  }

  public TraceNet create(long id)
  {
    if (id > 0)
    {
      TraceNet trace = tracesById.get(id);
      if (trace == null)
      {
        trace = new TraceNet(id);
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

