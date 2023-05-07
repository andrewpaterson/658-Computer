package net.logicim.ui.common.wire;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.List;

public interface WireView
{
  void connectTraces(CircuitSimulation simulation, List<Trace> traces);

  List<Trace> getTraces(CircuitSimulation simulation);

  List<ConnectionView> getConnections();

  View getView();

  void enable();

  void clearTraces(CircuitSimulation simulation);
}

