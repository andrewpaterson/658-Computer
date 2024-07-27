package net.logicim.ui.common;

import net.logicim.ui.common.wire.TraceView;

public class TraceOverlap
{
  protected LineOverlap overlap;
  protected TraceView traceView;

  public TraceOverlap(LineOverlap overlap, TraceView traceView)
  {
    this.overlap = overlap;
    this.traceView = traceView;
  }

  public TraceView getTraceView()
  {
    return traceView;
  }
}

