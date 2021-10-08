package name.bizna.bus.common;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.bus.common.TraceValue.Error;
import static name.bizna.bus.common.TraceValue.Undefined;

public class TraceNet
{
  protected List<Trace> traces;
  protected TraceValue value;

  public TraceNet(List<Trace> connected)
  {
    traces = connected;
    value = Undefined;
  }

  public TraceNet(Trace trace)
  {
    traces = new ArrayList<>();
    traces.add(trace);
    value = Undefined;
  }

  public void reset()
  {
    value = Undefined;
  }

  public TraceValue update(TraceValue value)
  {
    if (this.value == value)
    {
      return value;
    }
    else if (this.value == Undefined)
    {
      this.value = value;
      return value;
    }
    else
    {
      this.value = Error;
      return Error;
    }
  }

  public TraceValue getValue()
  {
    return value;
  }
}

