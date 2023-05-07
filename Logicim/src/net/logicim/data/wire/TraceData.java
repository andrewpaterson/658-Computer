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

  public TraceView create(SubcircuitEditor subcircuitEditor,
                          TraceLoader traceLoader,
                          boolean createConnections)
  {
    TraceView traceView = new TraceView(subcircuitEditor.getSubcircuitView(),
                                        start,
                                        end,
                                        createConnections);
    if (enabled)
    {
      traceView.enable();
    }

    if (createConnections)
    {
      WireDataHelper.wireConnect(subcircuitEditor,
                                 traceLoader,
                                 traceView,
                                 simulationTraces,
                                 selected);
    }
    return traceView;
  }

  public TraceView create(SubcircuitEditor subcircuitEditor)
  {
    return new TraceView(subcircuitEditor.getSubcircuitView(),
                         start,
                         end,
                         true);
  }

  public void createAndConnectComponent(SubcircuitEditor subcircuitEditor,
                                        CircuitSimulation circuitSimulation,
                                        TraceLoader traceLoader,
                                        TraceView traceView)
  {
    throw new SimulatorException();
  }
}

