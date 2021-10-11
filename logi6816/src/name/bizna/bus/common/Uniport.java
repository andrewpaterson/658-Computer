package name.bizna.bus.common;

import name.bizna.bus.gate.Tickable;
import name.bizna.util.EmulatorException;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.bus.common.TraceValue.Error;
import static name.bizna.bus.common.TraceValue.*;
import static name.bizna.bus.common.TransmissionState.*;

public class Uniport
    extends Port
{
  protected TraceValue value;
  protected Trace connection;

  public Uniport(Tickable tickable, String name)
  {
    super(tickable, name);
    value = Unsettled;
  }

  @Override
  public void resetConnections()
  {
    super.resetConnections();
    value = Unsettled;
    connection.getNet().reset();
  }

  @Override
  public void addTraceValues(List<TraceValue> traceValues)
  {
    traceValues.add(value);
  }

  @Override
  public void updateConnection()
  {
    if (connection != null)
    {
      if (state.isOutput())
      {
        value = connection.updateNetValue(value);
      }
    }
  }

  public boolean getBoolAfterRead()
  {
    if (state.isInput())
    {
      return value.isHigh();
    }
    else
    {
      throw new EmulatorException("Cannot read boolean value before port is set to Input.  Call and check the value of readState().");
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
      if (connection != null)
      {
        value = connection.getValue();
      }
      else
      {
        value = NotConnected;
      }
      return value;
    }
    else
    {
      throw new EmulatorException("Cannot read from a Port that is not an input.");
    }
  }

  @Override
  public List<Trace> getConnections()
  {
    ArrayList<Trace> connections = new ArrayList<>();
    connections.add(connection);
    return connections;
  }

  //A write is only done by the Tickable the Port exists in and causes the port ot be set as an output.
  public void writeBool(boolean value)
  {
    if (write())
    {
      this.value = fromBoolean(value);
    }
    else
    {
      throw new EmulatorException("Cannot write to a Port that is not an output.");
    }
  }

  public boolean write()
  {
    if (state.isNotSet())
    {
      state = Output;
    }

    return state.isOutput();
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
    this.connection = trace;
  }

  @Override
  public String getTraceValuesAsString()
  {
    return "" + value.getStringValue();
  }

  @Override
  public String getConnectionValuesAsString()
  {
    if (connection != null)
    {
      return "" + connection.getStringValue();
    }
    else
    {
      return " ";
    }
  }
}

