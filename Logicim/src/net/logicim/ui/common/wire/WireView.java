package net.logicim.ui.common.wire;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.ArrayList;
import java.util.List;

public abstract class WireView
    extends View
{
  protected List<Trace> traces;

  @Override
  public void enable(Simulation simulation)
  {
  }

  @Override
  public void disable()
  {
  }

  public void connectTraces(List<Trace> traces)
  {
    this.traces = traces;
  }

  public void disconnectTraces()
  {
    traces = new ArrayList<>();
  }

  public List<Trace> getTraces()
  {
    return traces;
  }
}

