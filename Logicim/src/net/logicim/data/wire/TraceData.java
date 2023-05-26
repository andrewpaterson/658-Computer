package net.logicim.data.wire;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Int2D;
import net.logicim.data.common.ViewData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.SubcircuitEditor;

import java.util.Map;

public class TraceData
    extends ViewData
{
  public Map<Long, long[]> simulationTraces;

  public Int2D start;
  public Int2D end;

  protected boolean enabled;
  protected boolean selected;

  public TraceData()
  {
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
    TraceView traceView = new TraceView(subcircuitEditor.getSubcircuitView(),
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

  public void createAndConnectComponent(CircuitSimulation circuitSimulation,
                                        TraceLoader traceLoader,
                                        TraceView traceView)
  {
    long[] traceIDs = simulationTraces.get(circuitSimulation.getId());
    if (traceIDs == null)
    {
      throw new SimulatorException("Cannot find trace IDs for Circuit Simulation [%s].", circuitSimulation.getDescription());
    }

    traceView.wireConnect(circuitSimulation,
                          traceLoader,
                          traceIDs
    );

  }
}

