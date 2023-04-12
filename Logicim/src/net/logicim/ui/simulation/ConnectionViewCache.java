package net.logicim.ui.simulation;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConnectionViewCache
{
  protected Map<Integer, Map<Integer, ConnectionView>> connectionViews;  //<X, <Y, Connection>>

  public ConnectionViewCache()
  {
    connectionViews = new LinkedHashMap<>();
  }

  public ConnectionView getOrAddConnectionView(Int2D position, View view)
  {
    ConnectionView connection = getConnectionView(position);
    if (connection != null)
    {
      connection.add(view);
      return connection;
    }
    else
    {
      return addConnectionView(position, view);
    }
  }

  public ConnectionView getConnectionView(Int2D position)
  {
    return getConnectionView(position.x, position.y);
  }

  protected ConnectionView addConnectionView(Int2D position, View view)
  {
    Map<Integer, ConnectionView> connectionViewMap = connectionViews.get(position.x);
    if (connectionViewMap == null)
    {
      connectionViewMap = new LinkedHashMap<>();
      connectionViews.put(position.x, connectionViewMap);
    }
    ConnectionView connectionView = new ConnectionView(view, position);
    connectionViewMap.put(position.y, connectionView);
    return connectionView;
  }

  public void removeConnectionView(View view, ConnectionView connectionView)
  {
    connectionView.remove(view);
    if (connectionView.isConnectedComponentsEmpty())
    {
      Int2D position = connectionView.getGridPosition();
      Map<Integer, ConnectionView> connectionViewMap = connectionViews.get(position.x);
      if (connectionViewMap != null)
      {
        ConnectionView cacheConnectionView = connectionViewMap.get(position.y);
        if (cacheConnectionView == connectionView)
        {
          connectionViewMap.remove(position.y);
          if (connectionViewMap.isEmpty())
          {
            connectionViews.remove(position.x);
          }
        }
        else
        {
          throw new SimulatorException("Cannot remove connection that is not in the connection cache.");
        }
      }
      else
      {
        throw new SimulatorException("Cannot remove connection that is not in the connection cache.");
      }
    }
  }

  public ConnectionView getConnectionView(int x, int y)
  {
    Map<Integer, ConnectionView> connectionViewMap = connectionViews.get(x);
    if (connectionViewMap != null)
    {
      return connectionViewMap.get(y);
    }
    return null;
  }

  public void validatePositions()
  {
    for (Map.Entry<Integer, Map<Integer, ConnectionView>> xEntry : connectionViews.entrySet())
    {
      int x = xEntry.getKey();
      Map<Integer, ConnectionView> connectionViewMap = xEntry.getValue();
      for (Map.Entry<Integer, ConnectionView> yEntry : connectionViewMap.entrySet())
      {
        int y = yEntry.getKey();
        ConnectionView connectionView = yEntry.getValue();
        Int2D connectionPosition = connectionView.getGridPosition();
        if (!connectionPosition.equals(x, y))
        {
          throw new SimulatorException("Connection map position in cache (%s) must be equal to connection position (%s).", new Int2D(x, y).toString(), connectionPosition.toString());
        }
      }
    }
  }

  public void removeAll(View view, List<ConnectionView> connectionViews)
  {
    for (ConnectionView connectionView : connectionViews)
    {
      removeConnectionView(view, connectionView);
    }
  }
}

