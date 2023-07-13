package net.logicim.ui.common.wire;

import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.voltage.VoltageColour;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WireViewComp
{
  protected List<ConnectionView> connections;
  protected Map<SubcircuitSimulation, List<Trace>> simulationTraces;

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

  protected Color getTraceColour(SubcircuitSimulation subcircuitSimulation)
  {
    if (subcircuitSimulation != null)
    {
      List<Trace> traces = simulationTraces.get(subcircuitSimulation);
      return VoltageColour.getColourForTraces(Colours.getInstance(), traces, subcircuitSimulation.getTime());
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

  protected Map<Long, long[]> save()
  {
    Map<Long, long[]> simulationTraces = new LinkedHashMap<>();
    for (Map.Entry<SubcircuitSimulation, List<Trace>> entry : this.simulationTraces.entrySet())
    {
      SubcircuitSimulation simulation = entry.getKey();
      List<Trace> traces = entry.getValue();
      long[] ids = new long[traces.size()];
      simulationTraces.put(simulation.getId(), ids);
      for (int i = 0; i < traces.size(); i++)
      {
        Trace trace = traces.get(i);
        ids[i] = Trace.getId(trace);
      }
    }
    return simulationTraces;
  }

  public void connectTraces(SubcircuitSimulation subcircuitSimulation, List<Trace> traces)
  {
    simulationTraces.put(subcircuitSimulation, traces);

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

    List<SubcircuitSimulation> subcircuitSimulations = new ArrayList<>(simulationTraces.keySet());
    for (SubcircuitSimulation simulation : subcircuitSimulations)
    {
      clearTraces(simulation);
    }
  }

  public void clearTraces(SubcircuitSimulation subcircuitSimulation)
  {
    simulationTraces.remove(subcircuitSimulation);
  }

  public List<Trace> getTraces(SubcircuitSimulation subcircuitSimulation)
  {
    return simulationTraces.get(subcircuitSimulation);
  }

  public void destroyComponent(SubcircuitSimulation subcircuitSimulation)
  {
    simulationTraces.remove(subcircuitSimulation);
  }
}

