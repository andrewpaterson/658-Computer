package net.logicim.ui.simulation;

import net.common.SimulatorException;
import net.common.type.Int2D;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.connection.PathConnectionView;

import java.util.*;

public class ConnectionViewCache
{
  protected Map<Integer, Map<Integer, ConnectionView>> connectionViews;  //<X, <Y, Connection>>

  protected Map<ViewPath, Map<ConnectionView, PathConnectionView>> pathConnectionViews;

  public ConnectionViewCache()
  {
    connectionViews = new LinkedHashMap<>();
    pathConnectionViews = new LinkedHashMap<>();
  }

  public ConnectionView getOrAddConnectionView(List<ViewPath> paths, Int2D position, View view)
  {
    ConnectionView connection = getConnectionView(position);
    if (connection != null)
    {
      connection.add(view);
      return connection;
    }
    else
    {
      return addConnectionView(paths, position, view);
    }
  }

  public ConnectionView getConnectionView(Int2D position)
  {
    return getConnectionView(position.x, position.y);
  }

  protected ConnectionView addConnectionView(List<ViewPath> paths, Int2D position, View view)
  {
    ConnectionView connectionView = cacheConnectionView(position, view);
    cachePathConnectionView(paths, connectionView);
    return connectionView;
  }

  private ConnectionView cacheConnectionView(Int2D position, View view)
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

  private void cachePathConnectionView(List<ViewPath> paths, ConnectionView connectionView)
  {
    for (ViewPath path : paths)
    {
      Map<ConnectionView, PathConnectionView> connectionViewMap = pathConnectionViews.get(path);
      if (connectionViewMap == null)
      {
        connectionViewMap = new LinkedHashMap<>();
        pathConnectionViews.put(path, connectionViewMap);
      }

      PathConnectionView pathConnectionView = connectionViewMap.get(connectionView);
      if (pathConnectionView == null)
      {
        pathConnectionView = new PathConnectionView(path, connectionView);
        connectionViewMap.put(connectionView, pathConnectionView);
      }
    }
  }

  public void removeConnectionView(List<ViewPath> paths, View view, ConnectionView connectionView)
  {
    boolean removed = removeConnectionView(view, connectionView);
    if (removed)
    {
      removePathConnectionView(paths, connectionView);
    }
  }

  protected boolean removeConnectionView(View view, ConnectionView connectionView)
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
          return true;
        }
        else
        {
          if (cacheConnectionView != null)
          {
            throw new SimulatorException("Cannot remove Connection View [%s] that does not match cached Connection View [%s].", connectionView, cacheConnectionView);
          }
          else
          {
            throw new SimulatorException("Cannot remove Connection View [%s] that is not in the cache.", connectionView);
          }
        }
      }
      else
      {
        throw new SimulatorException("Cannot remove connection that is not in the connection cache.");
      }
    }
    return false;
  }

  private void removePathConnectionView(List<ViewPath> paths, ConnectionView connectionView)
  {
    for (ViewPath path : paths)
    {
      Map<ConnectionView, PathConnectionView> pathConnectionViewMap = pathConnectionViews.get(path);
      if (pathConnectionViewMap != null)
      {
        PathConnectionView pathConnectionView = pathConnectionViewMap.remove(connectionView);
        if (pathConnectionView != null)
        {
          pathConnectionView.clear();
          if (pathConnectionViewMap.size() == 0)
          {
            pathConnectionViews.remove(path);
          }
        }
        else
        {
          throw new SimulatorException("Cannot remove path connection that is not in the path connection cache.");
        }
      }
      else
      {
        throw new SimulatorException("Cannot remove path connection that is not in the path connection cache.");
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

  public void removeAll(List<ViewPath> paths, View view, List<ConnectionView> connectionViews)
  {
    for (ConnectionView connectionView : connectionViews)
    {
      removeConnectionView(paths, view, connectionView);
    }
  }

  public List<ConnectionView> findAll()
  {
    List<ConnectionView> result = new ArrayList<>();
    for (Map.Entry<Integer, Map<Integer, ConnectionView>> xEntry : connectionViews.entrySet())
    {
      Map<Integer, ConnectionView> yMap = xEntry.getValue();
      for (Map.Entry<Integer, ConnectionView> yEntry : yMap.entrySet())
      {
        ConnectionView connectionView = yEntry.getValue();
        result.add(connectionView);
      }
    }
    return result;
  }

  public void addPaths(List<ViewPath> newPaths)
  {
    for (ViewPath path : newPaths)
    {
      if (pathConnectionViews.containsKey(path))
      {
        throw new SimulatorException("Cannot add new path [%s] into path connection cache.  It already exists.", path);
      }

      if (connectionViews.size() > 0)
      {
        LinkedHashMap<ConnectionView, PathConnectionView> pathConnectionViewMap = new LinkedHashMap<>();
        pathConnectionViews.put(path, pathConnectionViewMap);

        for (Map.Entry<Integer, Map<Integer, ConnectionView>> xEntry : connectionViews.entrySet())
        {
          Map<Integer, ConnectionView> connectionViewMap = xEntry.getValue();
          for (Map.Entry<Integer, ConnectionView> yEntry : connectionViewMap.entrySet())
          {
            ConnectionView connectionView = yEntry.getValue();
            PathConnectionView pathConnectionView = new PathConnectionView(path, connectionView);
            pathConnectionViewMap.put(connectionView, pathConnectionView);
          }
        }
      }
    }
  }

  public void removePaths(List<ViewPath> removedPaths)
  {
    for (ViewPath path : removedPaths)
    {
      Map<ConnectionView, PathConnectionView> connectionViewMap = pathConnectionViews.get(path);
      if (connectionViewMap == null)
      {
        throw new SimulatorException("Cannot remove path [%s] that is not in the path connection cache.", path);
      }

      for (PathConnectionView pathConnectionView : connectionViewMap.values())
      {
        pathConnectionView.clear();
      }
      pathConnectionViews.remove(path);
    }
  }

  public PathConnectionView getPathConnectionView(ViewPath path, ConnectionView connectionView)
  {
    Map<ConnectionView, PathConnectionView> pathConnectionViewMap = pathConnectionViews.get(path);
    if (pathConnectionViewMap != null)
    {
      return pathConnectionViewMap.get(connectionView);
    }
    return null;
  }

  public void validatePathConnections(List<ViewPath> paths)
  {
    if (connectionViews.size() > 0)
    {
      for (ViewPath path : paths)
      {
        Map<ConnectionView, PathConnectionView> connectionViewMap = pathConnectionViews.get(path);
        if (connectionViewMap == null)
        {
          throw new SimulatorException("View Connection Cache does not contain Subcircuit View Path [%s].", path);
        }
      }

      Set<ViewPath> pathsSet = new LinkedHashSet<>(paths);
      for (ViewPath path : pathConnectionViews.keySet())
      {
        if (!pathsSet.contains(path))
        {
          throw new SimulatorException("Subcircuit View does not contain View Connection Cache Path [%s].", path);
        }
      }

      for (Map.Entry<Integer, Map<Integer, ConnectionView>> xEntry : connectionViews.entrySet())
      {
        Map<Integer, ConnectionView> connectionViewMap = xEntry.getValue();
        for (Map.Entry<Integer, ConnectionView> yEntry : connectionViewMap.entrySet())
        {
          ConnectionView connectionView = yEntry.getValue();
          for (ViewPath path : paths)
          {
            Map<ConnectionView, PathConnectionView> pathConnectionViewMap = pathConnectionViews.get(path);
            PathConnectionView pathConnectionView = pathConnectionViewMap.get(connectionView);
            if (pathConnectionView == null)
            {
              throw new SimulatorException("Path [%s] connection view cache does contain Connection View [%s].", path.toString(), connectionView.toString());
            }
          }
        }
      }
      for (Map.Entry<ViewPath, Map<ConnectionView, PathConnectionView>> entry : pathConnectionViews.entrySet())
      {
        Map<ConnectionView, PathConnectionView> pathConnectionViewMap = entry.getValue();
        for (ConnectionView pathConnectionView : pathConnectionViewMap.keySet())
        {
          Int2D gridPosition = pathConnectionView.getGridPosition();
          ConnectionView cachedConnectionView = getConnectionView(gridPosition.getIntX(), gridPosition.getIntY());
          if (cachedConnectionView == null)
          {
            throw new SimulatorException("Connection view cache does not contain Path cache connection view [%s].", pathConnectionView.toString());
          }
          else if (pathConnectionView != cachedConnectionView)
          {
            throw new SimulatorException("Connection view cache connection view [%s] does not contain Path cache connection view [%s].", cachedConnectionView.toString(), pathConnectionView.toString());
          }
        }
      }
    }
    else
    {
      if (!pathConnectionViews.isEmpty())
      {
        throw new SimulatorException("Connection view path cache should be empty when there are [0] connections.");
      }
    }
  }
}

