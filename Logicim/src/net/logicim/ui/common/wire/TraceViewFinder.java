package net.logicim.ui.common.wire;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.connection.ConnectionFinder;

import java.util.LinkedHashSet;
import java.util.Set;

public class TraceViewFinder
    extends ConnectionFinder
{
  protected Set<TraceView> traceViews;

  public TraceViewFinder()
  {
    super();
    this.traceViews = new LinkedHashSet<>();
  }

  public void add(TraceView traceView)
  {
    traceViews.add(traceView);
    connectionsToProcess.addAll(traceView.getConnectionViews());
  }

  public Set<TraceView> getTraceViews()
  {
    return traceViews;
  }

  @Override
  protected void processTraceView(ConnectionView currentConnection, TraceView traceView)
  {
    super.processTraceView(currentConnection, traceView);
    traceViews.add(traceView);
  }

  @Override
  protected void processTunnelView(ConnectionView currentConnection, TunnelView tunnelView)
  {
  }
}

