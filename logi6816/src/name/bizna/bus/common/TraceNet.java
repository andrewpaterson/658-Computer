package name.bizna.bus.common;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.bus.common.TraceValue.Error;
import static name.bizna.bus.common.TraceValue.*;
import static name.bizna.util.DebugUtil.debug;
import static name.bizna.util.DebugUtil.debugLog;

public class TraceNet
{
  protected List<Trace> traces;
  protected TraceValue value;

  protected Port _DEBUG_lastPortThatUpdated;

  public TraceNet(List<Trace> connected)
  {
    traces = connected;
    value = Unsettled;

    _DEBUG_lastPortThatUpdated = null;
  }

  public TraceNet(Trace trace)
  {
    traces = new ArrayList<>();
    traces.add(trace);
    value = Unsettled;

    _DEBUG_lastPortThatUpdated = null;
  }

  public void reset()
  {
    value = Unsettled;

    _DEBUG_lastPortThatUpdated = null;
  }

  public TraceValue update(TraceValue value, Port port)
  {
    if (this.value == value)
    {
      _DEBUG_lastPortThatUpdated = port;
      return value;
    }
    else if (this.value == Unsettled || this.value == NotConnected)
    {
      _DEBUG_lastPortThatUpdated = port;
      this.value = value;
      return value;
    }
    else
    {
      if (debug)
      {
        debugLog("Trace conflict: [" + _DEBUG_lastPortThatUpdated.getDescription() + "] set net value [" + this.value.getStringValue() + "] but [" + port.getDescription() + "] set net value [" + value.getStringValue() + "].");
      }
      _DEBUG_lastPortThatUpdated = port;
      this.value = Error;
      return Error;
    }
  }

  public TraceValue getValue()
  {
    return value;
  }

  public Port get_DEBUG_lastPortThatUpdated()
  {
    return _DEBUG_lastPortThatUpdated;
  }
}

