package net.logicim.data.trace;

import net.logicim.common.type.Int2D;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.TraceView;

import java.util.Map;

public class TraceData
{
  public long id;

  public Int2D start;
  public Int2D end;

  public TraceData(long id, Int2D start, Int2D end)
  {
    this.id = id;
    this.start = start;
    this.end = end;
  }

  public TraceView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    TraceView traceView = new TraceView(circuitEditor, start, end);
    TraceNet trace = traceLoader.create(id);
    traceView.connect(trace);
    return traceView;
  }
}

