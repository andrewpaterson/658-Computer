package net.logicim.ui.common;

public class TraceOverlap
{
  protected LineOverlap overlap;
  protected TraceView traceView;

  public TraceOverlap(LineOverlap overlap, TraceView traceView)
  {
    this.overlap = overlap;
    this.traceView = traceView;
  }

  public LineOverlap getOverlap()
  {
    return overlap;
  }

  public TraceView getTraceView()
  {
    return traceView;
  }
}

