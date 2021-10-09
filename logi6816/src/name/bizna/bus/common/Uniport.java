package name.bizna.bus.common;

import name.bizna.bus.logic.Tickable;
import name.bizna.util.EmulatorException;

import java.util.List;

import static name.bizna.bus.common.TraceValue.Undefined;
import static name.bizna.bus.common.TraceValue.fromBoolean;

public class Uniport
    extends Port
{
  protected TransmissionState state;

  protected TraceValue value;
  protected Trace connection;

  public Uniport(Tickable tickable, String name)
  {
    super(tickable, name);
    state = TransmissionState.Undefined;
    value = Undefined;
  }

  @Override
  public void startPropagation()
  {
    state = TransmissionState.Undefined;
    value = Undefined;
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
      value = connection.updateNetValue(value);
    }
  }

  @Override
  public void resetConnection()
  {
    value = Undefined;
    connection.getNet().reset();
  }

  //A read is only done by the Tickable the Port exists in and causes the port to be set as an input.
  public boolean readBool()
  {
    TraceValue traceValue = readState();
    if (!traceValue.isValid())
    {
      throw new EmulatorException("Cannot read a boolean value from Port [" + name + "] that has invalid state [" + traceValue + "] in [" + tickable.getDescription() + "] .");
    }
    return traceValue.isHigh();
  }

  //A read is only done by the Tickable the Port exists in and causes the port to be set as an input.
  public TraceValue readState()
  {
    if (state == TransmissionState.Undefined)
    {
      state = TransmissionState.Input;
    }

    if (state == TransmissionState.Input)
    {
      if (connection != null)
      {
        value = connection.getValue();
      }
      else
      {
        value = Undefined;
      }
      return value;
    }
    else
    {
      throw new EmulatorException("Cannot read from a Port that is not an input.");
    }
  }

  //A write is only done by the Tickable the Port exists in and causes the port ot be set as an output.
  public void writeBool(boolean value)
  {
    if (state == TransmissionState.Undefined)
    {
      state = TransmissionState.Output;
    }

    if (state == TransmissionState.Output)
    {
      this.value = fromBoolean(value);
      ;
    }
    else
    {
      throw new EmulatorException("Cannot write to a Port that is not an output.");
    }
  }

  public void writeState(TraceValue value)
  {
    if (state == TransmissionState.Undefined)
    {
      state = TransmissionState.Output;
    }

    if (state == TransmissionState.Output)
    {
      this.value = value;
    }
    else
    {
      throw new EmulatorException("Cannot write to a Port that is not an output.");
    }
  }

  public void connect(Trace trace)
  {
    this.connection = trace;
  }
}

