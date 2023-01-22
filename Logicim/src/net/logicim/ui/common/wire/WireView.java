package net.logicim.ui.common.wire;

import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.List;

public interface WireView
{
  void connectTraces(List<Trace> traces);

  void disconnectTraces();

  List<Trace> getTraces();

  void removed();

  boolean isRemoved();

  List<ConnectionView> getConnections();

  View getView();
}

