package name.bizna.bus.common;

import name.bizna.bus.gate.Tickable;

import java.util.Collection;
import java.util.List;

import static name.bizna.bus.common.TraceValue.Error;
import static name.bizna.bus.common.TraceValue.*;
import static name.bizna.bus.common.TransmissionState.NotSet;

public abstract class Port
{
  protected Tickable tickable;
  protected String name;
  protected TransmissionState state;

  public Port(Tickable tickable, String name)
  {
    this.tickable = tickable;
    this.name = name;
    this.state = NotSet;
    tickable.addPort(this);
  }

  public void resetConnections()
  {
    state = NotSet;
  }

  public abstract void addTraceValues(List<TraceValue> traceValues);

  public abstract void updateConnection();

  public abstract TraceValue read();

  public static TraceValue readStates(Collection<? extends Port> ports)
  {
    boolean high = false;
    boolean low = false;
    boolean error = false;
    boolean unsettled = false;
    boolean connected = false;

    for (Port port : ports)
    {
      TraceValue value = port.read();
      if (value.isConnected())
      {
        connected = true;
      }

      if (value.isError())
      {
        error = true;
      }
      else if (value.isUnsettled())
      {
        unsettled = true;
      }
      else if (value.isHigh())
      {
        high = true;
      }
      else if (value.isLow())
      {
        low = true;
      }
    }

    return translatePortValue(high, low, error, unsettled, connected);
  }

  static TraceValue translatePortValue(boolean high, boolean low, boolean error, boolean unsettled, boolean connected)
  {
    if (connected)
    {
      if (error)
      {
        return Error;
      }
      else if (unsettled)
      {
        return Unsettled;
      }
      else if (high && low)
      {
        return HighAndLow;
      }
      else if (high)
      {
        return High;
      }
      else if (low)
      {
        return Low;
      }
    }
    return NotConnected;
  }
}

