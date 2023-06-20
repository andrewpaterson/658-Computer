package net.logicim.ui.common.wire;

import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.ArrayList;
import java.util.List;

public interface WireView
{
  void connectTraces(SubcircuitSimulation circuit, List<Trace> traces);

  List<Trace> getTraces(SubcircuitSimulation circuit);

  List<ConnectionView> getConnectionViews();

  View getView();

  void enable();

  void clearTraces(SubcircuitSimulation circuit);

  default void wireConnect(SubcircuitSimulation circuit,
                           TraceLoader traceLoader,
                           long[] traceIds)
  {
    List<Trace> traces = new ArrayList<>(traceIds.length);
    for (long id : traceIds)
    {
      Trace trace = traceLoader.create(id);
      traces.add(trace);
    }
    this.connectTraces(circuit, traces);
    this.enable();
  }
}

