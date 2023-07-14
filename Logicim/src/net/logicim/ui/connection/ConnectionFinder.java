package net.logicim.ui.connection;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ConnectionFinder
{
  protected List<ConnectionView> connectionsToProcess;
  protected Set<ConnectionView> connectionsNet;

  public ConnectionFinder()
  {
    connectionsToProcess = new ArrayList<>();
    connectionsNet = new LinkedHashSet<>();
  }

  public void addConnection(ConnectionView connection)
  {
    if (connection != null)
    {
      connectionsToProcess.add(connection);
    }
  }

  public void process()
  {
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
            processTunnelView(currentConnection, (TunnelView) view);
          }
          else if (view instanceof SubcircuitInstanceView)
          {
            processTraceSubcircuitInstanceView(currentConnection, (SubcircuitInstanceView) view);
          }
          else if (view instanceof PinView)
          {
            processPinView(currentConnection, (PinView) view);
          }
        }
      }
    }
  }

  protected void processTunnelView(ConnectionView currentConnection, TunnelView tunnelView)
  {
    List<ConnectionView> tunnelConnections = tunnelView.getAllConnectedTunnelConnections();
    for (ConnectionView connectionView : tunnelConnections)
    {
      if (connectionView != currentConnection)
      {
        addConnectionToProcess(connectionView);
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

  private void processTraceSubcircuitInstanceView(ConnectionView currentConnection, SubcircuitInstanceView subcircuitInstanceView)
  {
    PinView pinView = subcircuitInstanceView.getPinView(currentConnection);
    addConnectionToProcess(pinView.getPortView().getConnection());
  }

  private void processPinView(ConnectionView currentConnection, PinView pinView)
  {
  }

  public Set<ConnectionView> getConnections()
  {
    return connectionsNet;
  }
}

