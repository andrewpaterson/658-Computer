package net.logicim.ui.common.wire;

import net.logicim.ui.common.ConnectionView;

import java.util.LinkedHashSet;
import java.util.List;

public class TraceViewConnectionViewSetsPair
{
  protected LinkedHashSet<TraceView> traceViews;
  protected LinkedHashSet<ConnectionView> nonTraceViewConnectionViews;

  public TraceViewConnectionViewSetsPair()
  {
    traceViews = new LinkedHashSet<>();
    nonTraceViewConnectionViews = new LinkedHashSet<>();
  }

  public void addAll(List<TraceView> traceViews)
  {
    this.traceViews.addAll(traceViews);
  }

  public void add(ConnectionView connectionView)
  {
    nonTraceViewConnectionViews.add(connectionView);
  }

  public void add(TraceView traceView)
  {
    traceViews.add(traceView);
  }

  public LinkedHashSet<TraceView> getTraceViews()
  {
    return traceViews;
  }

  public LinkedHashSet<ConnectionView> getNonTraceViewConnectionViews()
  {
    return nonTraceViewConnectionViews;
  }
}

