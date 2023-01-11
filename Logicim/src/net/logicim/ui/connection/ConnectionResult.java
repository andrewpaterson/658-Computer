package net.logicim.ui.connection;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.wire.TraceView;

import java.util.List;
import java.util.Set;

public class ConnectionResult
{
  public ConnectionView initiatingConnection;

  public Set<ConnectionView> connections;
  public List<TraceView> traces;
}
