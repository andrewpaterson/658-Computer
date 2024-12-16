package net.logicim.ui.common.wire;

import net.common.SimulatorException;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.common.voltage.VoltageColour;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.Traces;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathComponentSimulation;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;

import java.awt.*;
import java.util.List;
import java.util.*;

public class WireViewComp
{
  protected List<ConnectionView> connections;
  protected ViewPathComponentSimulation<Traces> simulationWires;

  protected int busWidth;

  public WireViewComp()
  {
    connections = new ArrayList<>(2);
    connections.add(null);
    connections.add(null);

    this.simulationWires = new ViewPathComponentSimulation();
    this.busWidth = 1;
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
    return getStartConnection() != null &&
           getEndConnection() != null;
  }

  public List<ConnectionView> getConnections()
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
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
      Traces traces = simulationWires.getComponentSlow(subcircuitSimulation);
      if (traces != null)
      {
        return VoltageColour.getColourForTraces(Colours.getInstance(), traces.getTraces(), subcircuitSimulation.getTime());
      }
    }

    return Colours.getInstance().getDisconnectedTrace();
  }

  protected Stroke getTraceStroke(Viewport viewport)
  {
    if (busWidth == 1)
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
    Set<Map.Entry<ViewPath, Map<CircuitSimulation, Traces>>> entries = this.simulationWires.getEntrySet();
    for (Map.Entry<ViewPath, Map<CircuitSimulation, Traces>> pathEntry : entries)
    {
      Map<CircuitSimulation, Traces> circuitSimulations = pathEntry.getValue();
      for (Map.Entry<CircuitSimulation, Traces> circuitSimulationEntry : circuitSimulations.entrySet())
      {
        Traces traces = circuitSimulationEntry.getValue();
        SubcircuitSimulation containingSubcircuitSimulation = traces.getContainingSubcircuitSimulation();
        saveTraceIds(simulationTraces, traces, containingSubcircuitSimulation);
      }
    }

    return simulationTraces;
  }

  private void saveTraceIds(Map<Long, long[]> simulationTraces, Traces traces, SubcircuitSimulation simulation)
  {
    List<Trace> traceList = traces.getTraces();
    long[] ids = new long[traceList.size()];
    simulationTraces.put(simulation.getId(), ids);
    for (int i = 0; i < traceList.size(); i++)
    {
      Trace trace = traceList.get(i);
      ids[i] = Trace.getId(trace);
    }
  }

  public void connectTraces(ViewPath path, CircuitSimulation circuitSimulation, List<Trace> traces)
  {
    SubcircuitSimulation subcircuitSimulation = path.getSubcircuitSimulation(circuitSimulation);
    if (simulationWires.get(path, circuitSimulation) != null)
    {
      throw new SimulatorException("Cannot connect traces.  Already connected.");
    }
    simulationWires.put(path, circuitSimulation, new Traces(traces, subcircuitSimulation));

    busWidth = calculateBusWidth();
  }

  protected int calculateBusWidth()
  {
    int busWidth = Integer.MAX_VALUE;
    List<Traces> traceList = simulationWires.getComponents();
    for (Traces traces : traceList)
    {
      if (traces.size() < busWidth)
      {
        busWidth = traces.size();
      }
    }

    if (busWidth == Integer.MAX_VALUE)
    {
      busWidth = 1;
    }
    return busWidth;
  }

  public void disconnectViewAndDestroyComponents()
  {
    for (int i = 0; i < connections.size(); i++)
    {
      connections.set(i, null);
    }

    destroyAllComponents();
  }

  public Traces getTraces(ViewPath path, CircuitSimulation circuitSimulation)
  {
    return simulationWires.get(path, circuitSimulation);
  }

  public Set<? extends SubcircuitSimulation> getWireSubcircuitSimulations()
  {
    return simulationWires.getSimulations();
  }

  public void destroyAllComponents()
  {
    simulationWires.clear();
  }

  public void destroyComponent(ViewPath path, CircuitSimulation circuitSimulation)
  {
    simulationWires.remove(path, circuitSimulation);
  }
}

