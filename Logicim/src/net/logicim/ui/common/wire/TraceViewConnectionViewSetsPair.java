package net.logicim.ui.common.wire;

import net.logicim.ui.common.ConnectionView;

import java.util.LinkedHashSet;
import java.util.List;

public class TraceViewConnectionViewSetsPair
{
  protected LinkedHashSet<TraceView> traceViews;
  protected LinkedHashSet<ConnectionView> nonTraceConnectionViews;

  public TraceViewConnectionViewSetsPair()
  {
    traceViews = new LinkedHashSet<>();
    nonTraceConnectionViews = new LinkedHashSet<>();
  }

  public void addAll(List<TraceView> traceViews)
  {
    this.traceViews.addAll(traceViews);
  }

  public void add(ConnectionView connectionView)
  {
    nonTraceConnectionViews.add(connectionView);
  }

  public void add(TraceView traceView)
  {
    traceViews.add(traceView);
  }

  public LinkedHashSet<TraceView> getTraceViews()
  {
    return traceViews;
  }

  public LinkedHashSet<ConnectionView> getNonTraceConnectionViews()
  {
    return nonTraceConnectionViews;
  }
}

