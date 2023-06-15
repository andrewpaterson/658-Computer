package net.logicim.ui.common.wire;

import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.InstanceCircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.ArrayList;
import java.util.List;

public interface WireView
{
  void connectTraces(InstanceCircuitSimulation circuit, List<Trace> traces);

  List<Trace> getTraces(InstanceCircuitSimulation circuit);

  List<ConnectionView> getConnectionViews();

  View getView();

  void enable();

  void clearTraces(InstanceCircuitSimulation circuit);

  default void wireConnect(InstanceCircuitSimulation circuit,
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

