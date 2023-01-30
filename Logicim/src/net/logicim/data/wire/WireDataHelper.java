package net.logicim.data.wire;

import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.simulation.CircuitEditor;

import java.util.ArrayList;
import java.util.List;

public abstract class WireDataHelper
{
  protected static void wireConnect(CircuitEditor circuitEditor,
                                 TraceLoader traceLoader,
                                 WireView wireView,
                                 long[] traceIds,
                                 boolean selected)
  {
    List<Trace> traces = new ArrayList<>(traceIds.length);
    for (long id : traceIds)
    {
      Trace trace = traceLoader.create(id);
      traces.add(trace);
    }
    wireView.connectTraces(traces);
    wireView.enable(circuitEditor.getSimulation());

    if (selected)
    {
      circuitEditor.select(wireView.getView());
    }
  }
}

