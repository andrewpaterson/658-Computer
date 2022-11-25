package net.logicim.data.trace;

import net.logicim.common.type.Int2D;
import net.logicim.data.SaveData;
import net.logicim.data.common.Int2DData;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.TraceView;

public class TraceData
    extends SaveData
{
  public long id;

  public Int2DData start;
  public Int2DData end;

  public TraceData(long id, Int2D start, Int2D end)
  {
    this.id = id;
    this.start = new Int2DData(start);
    this.end = new Int2DData(end);
  }

  public TraceView create(CircuitEditor circuitEditor, TraceLoader traceLoader)
  {
    TraceView traceView = new TraceView(circuitEditor, start.toInt2D(), end.toInt2D());
    TraceNet trace = traceLoader.create(id);
    traceView.connect(trace);
    return traceView;
  }
}

