package net.simulation.common;

import java.util.Collection;
import java.util.List;

import static net.simulation.common.TraceValue.Error;
import static net.simulation.common.TraceValue.*;
import static net.simulation.common.TransmissionState.NotSet;

public abstract class Port
{
  protected TickablePins tickable;
  protected String name;
  protected TransmissionState state;

  public Port(TickablePins tickable, String name)
  {
    this.tickable = tickable;
    this.name = name;
    this.state = NotSet;
    tickable.addPort(this);
  }

  public String getName()
  {
    return name;
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

  public String getPortTransmissionStateAsString()
  {
    String portStateString = "  ";
    if (state.isInput())
    {
      portStateString = "<-";
    }
    else if (state.isOutput())
    {
      portStateString = "->";
    }
    else if (state.isNotSet())
    {
      portStateString = "..";
    }
    else if (state.isImpedance())
    {
      portStateString = "xx";
    }
    return getName() + "[" + portStateString + "]";
  }

  public String getDescription()
  {
    return getTickable().getDescription() + "." + getName();
  }

  public TickablePins getTickable()
  {
    return tickable;
  }

  public abstract List<Trace> getConnections();

  public abstract String getTraceValuesAsString();

  public abstract String getWireValuesAsString();

  public abstract String getConnectionValuesAsString();
}

