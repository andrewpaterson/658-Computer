package net.logicim.data.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.common.ReflectiveData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.simulation.SubcircuitEditor;

public class TraceData
    extends ReflectiveData
{
  public long[] traceIds;

  public Int2D start;
  public Int2D end;
  public long id;

  protected boolean selected;

  public TraceData()
  {
  }

  public TraceData(long[] traceIds,
                   Int2D start,
                   Int2D end,
                   long id,
                   boolean selected)
  {
    this.traceIds = traceIds;

    this.start = start.clone();
    this.end = end.clone();
    this.id = id;
    this.selected = selected;
  }

  public TraceView create(SubcircuitEditor subcircuitEditor,
                          CircuitSimulation simulation,
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

  public TraceView create(SubcircuitEditor subcircuitEditor)
  {
    return new TraceView(subcircuitEditor.getSubcircuitView(),
                         start,
                         end,
                         true);
  }
}

