package net.logicim.ui.common.wire;

import net.logicim.domain.common.wire.Trace;

import java.util.List;

public interface WireView
{
  void connectTraces(List<Trace> traces);

  void disconnectTraces();

  List<Trace> getTraces();
}

