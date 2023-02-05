package net.logicim.ui.common.wire;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.connection.ConnectionFinder;

import java.util.LinkedHashSet;
import java.util.Set;

public class TraceFinder
    extends ConnectionFinder
{
  protected Set<TraceView> traceViews;

  public TraceFinder()
  {
    super();
    this.traceViews = new LinkedHashSet<>();
  }

  public void add(TraceView traceView)
  {
    traceViews.add(traceView);
    connectionsToProcess.addAll(traceView.getConnections());
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
  protected void processTunnel(ConnectionView currentConnection, TunnelView tunnelView)
  {
  }
}

