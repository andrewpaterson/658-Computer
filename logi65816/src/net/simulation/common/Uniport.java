package net.simulation.common;

import net.simulation.gate.Tickable;
import net.util.EmulatorException;

import java.util.ArrayList;
import java.util.List;

import static net.simulation.common.TraceValue.Error;
import static net.simulation.common.TraceValue.*;
import static net.simulation.common.TransmissionState.*;

public class Uniport
    extends Port
{
  protected TraceValue pin;
  protected Trace wire;

  public Uniport(Tickable tickable, String name)
  {
    super(tickable, name);
    pin = Unsettled;
  }

  @Override
  public void resetConnections()
  {
    super.resetConnections();
    pin = Unsettled;
    wire.getNet().reset();
  }

  @Override
  public void addTraceValues(List<TraceValue> traceValues)
  {
    traceValues.add(pin);
  }

  @Override
  public void updateConnection()
  {
    if (wire != null)
    {
      if (state.isOutput())
      {
        pin = wire.updateNetValue(pin, this);
      }
    }
  }

  public boolean getBoolAfterRead()
  {
    if (state.isInput())
    {
      return pin.isHigh();
    }
    else
    {
      throw new EmulatorException("Cannot read a boolean value from Port [" + getDescription() + "] that has an invalid value [" + pin.toEnumString() + "].");
    }
  }

  //A read is only done by the Tickable the Port exists in and causes the port to be set as an input.
  public TraceValue read()
  {
    if (state.isNotSet())
    {
      state = Input;
    }

    if (state.isInput())
    {
      if (wire != null)
      {
        pin = wire.getValue();
      }
      else
      {
        pin = NotConnected;
      }
      return pin;
    }
    else
    {
      throw new EmulatorException("Cannot read from Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  @Override
  public List<Trace> getConnections()
  {
    ArrayList<Trace> connections = new ArrayList<>();
    connections.add(wire);
    return connections;
  }

  //A write is only done by the Tickable the Port exists in and causes the port ot be set as an output.
  public void writeBool(boolean value)
  {
    if (state.isNotSet())
    {
      state = Output;
    }

    if (state.isOutput())
    {
      pin = fromBoolean(value);
    }
    else
    {
      throw new EmulatorException("Cannot write to Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  public void unset()
  {
    state = NotSet;
    pin = Unsettled;
  }

  public void error()
  {
    state = NotSet;  //Maybe?  Maybe we need an error state.
    pin = Error;
  }

  public void connect(Trace trace)
  {
    this.wire = trace;
  }

  @Override
  public String getTraceValuesAsString()
  {
    return "" + pin.getStringValue();
  }

  @Override
  public String getConnectionValuesAsString()
  {
    if (wire != null)
    {
      return "" + wire.getStringValue();
    }
    else
    {
      return " ";
    }
  }
}

