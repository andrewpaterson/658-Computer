package net.logicim.data.wire;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.wire.WireView;

import java.util.ArrayList;
import java.util.List;

public abstract class WireDataHelper
{
  protected static void wireConnect(CircuitSimulation simulation,
                                    TraceLoader traceLoader,
                                    WireView wireView,
                                    long[] traceIds)
  {
    List<Trace> traces = new ArrayList<>(traceIds.length);
    for (long id : traceIds)
    {
      Trace trace = traceLoader.create(id);
      traces.add(trace);
    }
    wireView.connectTraces(simulation, traces);
    wireView.enable();
  }
}

