package net.logicim.data.wire;

import net.common.SimulatorException;
import net.common.type.Int2D;
import net.logicim.data.common.ViewData;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.Map;

public class TraceData
    extends ViewData
{
  public Map<Long, long[]> simulationTraces;

  public Int2D start;
  public Int2D end;

  public boolean enabled;
  public boolean selected;

  public TraceData()
  {
    super();
  }

  @Override
  public boolean appliesToSimulation(long id)
  {
    return simulationTraces.containsKey(id);
  }

  public TraceData(Map<Long, long[]> simulationTraces,
                   Int2D start,
                   Int2D end,
                   long id,
                   boolean enabled,
                   boolean selected)
  {
    super(id);
    this.simulationTraces = simulationTraces;

    this.start = start.clone();
    this.end = end.clone();
    this.enabled = enabled;
    this.selected = selected;
  }

  public TraceView createAndEnableTraceView(SubcircuitEditor subcircuitEditor)
  {
    TraceView traceView = new TraceView(subcircuitEditor.getCircuitSubcircuitView(),
                                        start,
                                        end,
                                        true);
    if (enabled)
    {
      traceView.enable();
    }
    if (selected)
    {
      subcircuitEditor.select(traceView.getView());
    }

    return traceView;
  }

  public void createAndConnectComponentDuringLoad(SubcircuitSimulation subcircuitSimulation,
                                                  TraceLoader traceLoader,
                                                  TraceView traceView)
  {
    long[] traces = simulationTraces.get(subcircuitSimulation.getId());
    if (traces == null)
    {
      throw new SimulatorException("Cannot find trace IDs for Circuit Simulation [%s].", subcircuitSimulation.getDescription());
    }

    traceView.wireConnectDuringLoad(subcircuitSimulation, traceLoader, traces);
  }
}

