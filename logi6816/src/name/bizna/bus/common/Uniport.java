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
      throw new EmulatorException("Cannot read boolean value before port is set to Input.  Call and check the value of readState().");
    }
  }

  //A read is only done by the Tickable the Port exists in and causes the port to be set as an input.
  public TraceValue read()
  {
    state = Input;

    if (wire != null)
    {
      pin = wire.getValue();
    }
    else
    {
      pin = NotConnected;
    }
    return pin;
//    else
//    {
//      throw new EmulatorException("Cannot read from a Port that is not an input.");
//    }
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
    state = Output;
    pin = fromBoolean(value);
//    else
//    {
//      throw new EmulatorException("Cannot write to a Port that is not an output.");
//    }
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

