package net.logicim.data.wire;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.wire.WireView;

import java.util.ArrayList;
import java.util.List;

public abstract class WireDataHelper
{
  protected static void wireConnect(SubcircuitView subcircuitView,
                                    Simulation simulation,
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
    wireView.enable(simulation);

    if (selected)
    {
      subcircuitView.select(wireView.getView());
    }
  }
}

