package net.logicim.data.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.ReflectiveData;
import net.logicim.domain.Simulation;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.SubcircuitEditor;

public class TraceData
    extends ReflectiveData
{
  public long[] traceIds;

  public Int2D start;
  public Int2D end;

  protected boolean selected;

  public TraceData()
  {
  }

  public TraceData(long[] traceIds,
                   Int2D start,
                   Int2D end,
                   boolean selected)
  {
    this.traceIds = traceIds;

    this.start = start.clone();
    this.end = end.clone();
    this.selected = selected;
  }

  public TraceView create(SubcircuitEditor subcircuitEditor,
                          Simulation simulation,
                          TraceLoader traceLoader,
                          boolean createConnections)
  {
    TraceView traceView = new TraceView(subcircuitEditor.getSubcircuitView(),
                                        start,
                                        end,
                                        createConnections);
    if (createConnections)
    {
      WireDataHelper.wireConnect(subcircuitEditor,
                                 simulation,
                                 traceLoader,
                                 traceView,
                                 traceIds,
                                 selected);
    }
    return traceView;
  }
}

