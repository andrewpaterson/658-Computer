package net.logicim.ui.common.wire;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.List;

public interface WireView
{
  void connectTraces(List<Trace> traces);

  List<Trace> getTraces();

  List<ConnectionView> getConnections();

  View getView();

  void enable(Simulation simulation);

  void clearTraces();
}

