package net.logicim.ui.common.wire;

import net.logicim.ui.common.ConnectionView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TraceViewConnectionViewSetsPair
{
  protected Set<TraceView> traceViews;
  protected Set<ConnectionView> nonTraceConnectionViews;

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

  public Set<TraceView> getTraceViews()
  {
    return traceViews;
  }

  public Set<ConnectionView> getNonTraceConnectionViews()
  {
    return nonTraceConnectionViews;
  }
}

