package net.logicim.data.trace;

import net.logicim.common.type.Int2D;
import net.logicim.data.ReflectiveData;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.trace.TraceView;

public class TraceData
    extends ReflectiveData
{
  public long id;

  public Int2D start;
  public Int2D end;

  protected boolean selected;

  public TraceData()
  {
  }

  public TraceData(long id, Int2D start, Int2D end, boolean selected)
  {
    this.id = id;
    this.start = start.clone();
    this.end = end.clone();
    this.selected = selected;
  }

  public TraceView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    TraceView traceView = new TraceView(circuitEditor, start, end);
    TraceNet trace = traceLoader.create(id);
    traceView.connectTraceNet(trace);

    if (selected)
    {
      circuitEditor.select(traceView);
    }

    return traceView;
  }
}

