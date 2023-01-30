package net.logicim.ui.connection;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TunnelView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ConnectionFinder
{
  protected ArrayList<ConnectionView> connectionsToProcess;
  protected Set<ConnectionView> connectionsNet;

  public ConnectionFinder()
  {
    connectionsToProcess = new ArrayList<>();
    connectionsNet = new LinkedHashSet<>();
  }

  public void findConnections(ConnectionView connection)
  {
    if (connection != null)
    {
      connectionsToProcess.add(connection);
      while (connectionsToProcess.size() > 0)
      {
        ConnectionView currentConnection = connectionsToProcess.get(0);
        connectionsToProcess.remove(0);
        connectionsNet.add(currentConnection);

        List<View> components = currentConnection.getConnectedComponents();
        if (components != null)
        {
          for (View view : components)
          {
            if (view instanceof TraceView)
            {
              processTraceView(currentConnection, (TraceView) view);
            }
            else if (view instanceof TunnelView)
            {
              processTunnel(currentConnection, (TunnelView) view);
            }
          }
        }
      }
    }
  }

  protected void processTunnel(ConnectionView currentConnection, TunnelView tunnelView)
  {
    Set<TunnelView> tunnels = tunnelView.getTunnels();
    if (tunnels != null)
    {
      for (TunnelView tunnel : tunnels)
      {
        for (ConnectionView connection : tunnel.getConnections())
        {
          if (connection != currentConnection)
          {
            addConnectionToProcess(connection);
          }
        }
      }
    }
  }

  protected void processTraceView(ConnectionView currentConnection, TraceView traceView)
  {
    ConnectionView connectionView = traceView.getOpposite(currentConnection);
    addConnectionToProcess(connectionView);
  }

  protected void addConnectionToProcess(ConnectionView connection)
  {
    if (connection != null && !connectionsNet.contains(connection))
    {
      connectionsToProcess.add(connection);
    }
  }

  public Set<ConnectionView> getConnections()
  {
    return connectionsNet;
  }
}

