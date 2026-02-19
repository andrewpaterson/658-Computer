package net.logicim.ui.common.wire;

import net.logicim.data.wire.TraceLoader;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.Traces;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface WireView
{
  void connectTraces(ViewPath viewPath, CircuitSimulation circuitSimulation, Set<Trace> traces);

  Traces getTraces(ViewPath viewPath, CircuitSimulation circuitSimulation);

  List<ConnectionView> getConnectionViews();

  View getView();

  void enable();

  void destroyAllComponents();

  void destroyComponent(ViewPath viewPath, CircuitSimulation circuitSimulation);

  String getDescription();

  WireViewComp getWireViewComp();

  default Set<? extends SubcircuitSimulation> getWireSubcircuitSimulations()
  {
    return getWireViewComp().getWireSubcircuitSimulations();
  }

  default void wireConnectDuringLoad(ViewPath viewPath, CircuitSimulation circuitSimulation,
                                     TraceLoader traceLoader,
                                     long[] traceIds)
  {
    Set<Trace> traces = new LinkedHashSet<>(traceIds.length);
    for (long id : traceIds)
    {
      Trace trace = traceLoader.create(id);
      traces.add(trace);
    }
    this.connectTraces(viewPath, circuitSimulation, traces);
    this.enable();
  }
}

