package net.logicim.data.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.domain.common.wire.Trace;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.common.wire.TraceView;

import java.util.ArrayList;
import java.util.List;

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

  public TraceData(long[] traceIds, Int2D start, Int2D end, boolean selected)
  {
    this.traceIds = traceIds;

    this.start = start.clone();
    this.end = end.clone();
    this.selected = selected;
  }

  public void create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    TraceView traceView = new TraceView(circuitEditor, start, end);
    List<Trace> traces = new ArrayList<>(traceIds.length);
    for (long id : traceIds)
    {
      Trace trace = traceLoader.create(id);
      traces.add(trace);
    }
    traceView.connectTraces(traces);

    if (selected)
    {
      circuitEditor.select(traceView);
    }
  }
}

