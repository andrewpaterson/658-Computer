package net.logicim.data.trace;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.domain.common.trace.Trace;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.wire.TraceView;

import java.util.ArrayList;
import java.util.List;

public class TraceData
    extends ReflectiveData
{
  public long[] ids;

  public Int2D start;
  public Int2D end;

  protected boolean selected;

  public TraceData()
  {
  }

  public TraceData(long[] ids, Int2D start, Int2D end, boolean selected)
  {
    this.ids = ids;

    this.start = start.clone();
    this.end = end.clone();
    this.selected = selected;
  }

  public TraceView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    TraceView traceView = new TraceView(circuitEditor, start, end);
    List<Trace> traces = new ArrayList<>(ids.length);
    for (long id : ids)
    {
      Trace trace = traceLoader.create(id);
      traces.add(trace);
    }
    traceView.connectTraceNet(traces);

    if (selected)
    {
      circuitEditor.select(traceView);
    }

    return traceView;
  }
}

