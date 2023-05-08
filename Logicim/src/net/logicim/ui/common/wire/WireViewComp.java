package net.logicim.ui.common.wire;

import net.logicim.data.common.LongArrayData;
import net.logicim.data.common.LongData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.VoltageColour;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WireViewComp
{
  protected List<ConnectionView> connections;
  protected Map<CircuitSimulation, List<Trace>> simulationTraces;

  protected int width;

  public WireViewComp()
  {
    connections = new ArrayList<>(2);
    connections.add(null);
    connections.add(null);
    this.simulationTraces = new LinkedHashMap<>();
    this.width = 1;
  }

  public void setStart(ConnectionView connectionView)
  {
    connections.set(0, connectionView);
  }

  public void setEnd(ConnectionView connectionView)
  {
    connections.set(1, connectionView);
  }

  public ConnectionView getStartConnection()
  {
    return connections.get(0);
  }

  public ConnectionView getEndConnection()
  {
    return connections.get(1);
  }

  public boolean hasConnections()
  {
    return getStartConnection() != null && getEndConnection() != null;
  }

  public List<ConnectionView> getConnections()
  {
    boolean noneNull = true;
    for (ConnectionView connection : connections)
    {
      if (connection == null)
      {
        noneNull = false;
        break;
      }
    }

    if (noneNull)
    {
      return connections;
    }

    ArrayList<ConnectionView> connectionViews = new ArrayList<>();
    for (ConnectionView connection : connections)
    {
      if (connection != null)
      {
        connectionViews.add(connection);
      }
    }
    return connectionViews;
  }

  protected Color getTraceColour(CircuitSimulation simulation)
  {
    if (simulation != null)
    {
      List<Trace> traces = simulationTraces.get(simulation);
      return VoltageColour.getColourForTraces(Colours.getInstance(), traces, simulation.getTime());
    }
    else
    {
      return Colours.getInstance().getDisconnectedTrace();
    }
  }

  protected Stroke getTraceStroke(Viewport viewport)
  {
    if (width == 1)
    {
      return viewport.getZoomableStroke();
    }
    else
    {
      return viewport.getZoomableStroke(3.0f);
    }
  }

  protected Map<LongData, LongArrayData> save()
  {
    Map<LongData, LongArrayData> simulationTraces = new LinkedHashMap<>();
    for (Map.Entry<CircuitSimulation, List<Trace>> entry : this.simulationTraces.entrySet())
    {
      CircuitSimulation simulation = entry.getKey();
      List<Trace> traces = entry.getValue();
      long[] ids = new long[traces.size()];
      simulationTraces.put(new LongData( simulation.getId()), new LongArrayData(ids));
      for (int i = 0; i < traces.size(); i++)
      {
        Trace trace = traces.get(i);
        ids[i] = Trace.getId(trace);
      }
    }
    return simulationTraces;
  }

  public void connectTraces(CircuitSimulation simulation, List<Trace> traces)
  {
    simulationTraces.put(simulation, traces);

    width = Integer.MAX_VALUE;
    for (List<Trace> existing : simulationTraces.values())
    {
      if (existing.size() < width)
      {
        width = existing.size();
      }
    }
    if (width == Integer.MAX_VALUE)
    {
      width = 1;
    }
  }

  public void disconnect()
  {
    for (int i = 0; i < connections.size(); i++)
    {
      connections.set(i, null);
    }

    for (CircuitSimulation simulation : simulationTraces.keySet())
    {
      clearTraces(simulation);
    }
  }

  public void clearTraces(CircuitSimulation simulation)
  {
    simulationTraces.remove(simulation);
  }

  public List<Trace> getTraces(CircuitSimulation simulation)
  {
    return simulationTraces.get(simulation);
  }
}

