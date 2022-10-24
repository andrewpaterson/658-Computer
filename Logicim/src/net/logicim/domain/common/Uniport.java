package net.logicim.domain.common;

import net.logicim.common.SimulatorException;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.TraceValue.Unsettled;
import static net.logicim.domain.common.TransmissionState.*;

public class Uniport
    extends Port
{
  protected TraceValue value;
  protected Trace wire;

  public Uniport(Pins tickable, String name)
  {
    super(tickable, name);
    value = Unsettled;
  }

  @Override
  public void resetConnections()
  {
    super.resetConnections();
    value = Unsettled;
    wire.getNet().unsettle();
  }

  public void addTraceValues(List<TraceValue> traceValues)
  {
    traceValues.add(value);
  }

  public void updateConnection()
  {
    if (wire != null)
    {
      if (state.isOutput())
      {
        value = wire.update(value, this);
      }
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
        value = wire.getVoltage();
      }
      else
      {
        value = NotConnected;
      }
      return value;
    }
    else
    {
      throw new SimulatorException("Cannot read from Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  @Override
  public List<Trace> getConnections()
  {
    ArrayList<Trace> connections = new ArrayList<>();
    connections.add(wire);
    return connections;
  }

  public Timeline getTimeline()
  {
    return pins.getTimeline();
  }

  public void writeBool(boolean value)
  {
    if (state.isNotSet())
    {
      state = Output;
    }

    if (state.isOutput())
    {
      throw new SimulatorException("Not Yet Implemented");
    }
    else
    {
      throw new SimulatorException("Cannot write to Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  public void unset()
  {
    state = NotSet;
    value = Unsettled;
  }

  public void error()
  {
    state = NotSet;  //Maybe?  Maybe we need an error state.
    value = Error;
  }

  public void connect(Trace trace)
  {
    this.wire = trace;
  }

  public void highImpedance()
  {
    if (state.isNotSet())
    {
      state = Impedance;
    }

    if (!state.isImpedance())
    {
      throw new SimulatorException("Cannot high-impedance Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  @Override
  public String getTraceValuesAsString()
  {
    return "" + value.getStringValue();
  }

  @Override
  public String getWireValuesAsString()
  {
    if (wire != null)
    {
      if (wire.isNotConnected())
      {
        return " ";
      }
      else
      {
        return "" + wire.toString();
      }
    }
    else
    {
      return " ";
    }
  }

  @Override
  public String getConnectionValuesAsString()
  {
    return getWireValuesAsString();
  }
}

