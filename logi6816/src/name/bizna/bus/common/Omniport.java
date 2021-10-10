package name.bizna.bus.common;

import name.bizna.bus.logic.Tickable;
import name.bizna.util.EmulatorException;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.bus.common.TraceValue.Error;
import static name.bizna.bus.common.TraceValue.*;
import static name.bizna.bus.common.TransmissionState.*;

public class Omniport
    extends Port
{
  protected TransmissionState state;

  protected List<TraceValue> pins;
  protected List<Trace> connections;

  public Omniport(Tickable tickable, String name, int width)
  {
    super(tickable, name);
    state = NotSet;
    connections = new ArrayList<>();
    pins = new ArrayList<>(width);

    for (int i = 0; i < width; i++)
    {
      pins.add(Undefined);
      connections.add(null);
    }
  }

  public void resetConnections()
  {
    state = NotSet;

    int length = pins.size();
    for (int i = 0; i < length; i++)
    {
      pins.set(i, Undefined);
      Trace trace = connections.get(i);
      if (trace != null)
      {
        trace.getNet().reset();
      }
    }
  }

  @Override
  public void addTraceValues(List<TraceValue> traceValues)
  {
    traceValues.addAll(pins);
  }

  @Override
  public void updateConnection()
  {
    int length = pins.size();
    for (int i = 0; i < length; i++)
    {
      TraceValue value = pins.get(i);
      Trace trace = connections.get(i);
      if (trace != null)
      {
        value = trace.updateNetValue(value);
        pins.set(i, value);
      }
    }
  }

  public TraceValue readSinglePinState(int pin)
  {
    if (pin >= 0 && pin < pins.size())
    {
      if (state == NotSet)
      {
        state = Input;
      }

      if (state == Input)
      {
        Trace connection = connections.get(pin);
        TraceValue value;
        if (connection != null)
        {
          value = connection.getValue();
        }
        else
        {
          value = Undefined;
        }
        pins.set(pin, value);
        return value;
      }
      else
      {
        throw new EmulatorException("Cannot read from a Port that is not an input.");
      }

    }
    else
    {
      throw new EmulatorException("Pin number exceeds port width.");
    }
  }

  public long getPinsAsBoolAfterRead()
  {
    if (state == Input)
    {
      long value = 0;
      for (int i = pins.size() - 1; i >= 0; i--)
      {
        TraceValue traceValue = pins.get(i);
        value <<= 1;
        if (traceValue.isHigh())
        {
          value |= 1;
        }
        else if (traceValue.isInvalid())
        {
          throw new EmulatorException("Cannot read a boolean value from Port [" + name + "] that has invalid state [" + traceValue + "] in [" + tickable.getDescription() + "] .");
        }
      }
      return value;
    }
    else
    {
      throw new EmulatorException("Cannot read from a Port that is not an input.");
    }
  }

  public void writeSinglePinState(int pin, TraceValue value)
  {
    if (pin >= 0 && pin < pins.size())
    {
      if (state == NotSet)
      {
        state = Output;
      }

      if (state == Output)
      {
        pins.set(pin, value);
      }
      else
      {
        throw new EmulatorException("Cannot write to a Port that is not an output.");
      }
    }
    else
    {
      throw new EmulatorException("Pin number exceeds port width.");
    }
  }

  public void writeAllPinsBool(long longValue)
  {
    if (state == NotSet)
    {
      state = Output;
    }

    if (state == Output)
    {
      for (int i = 0; i < pins.size(); i++)
      {
        TraceValue value = Low;
        if ((longValue >> i & 1) == 1)
        {
          value = High;
        }

        pins.set(i, value);
      }
    }
    else
    {
      throw new EmulatorException("Cannot write to a Port that is not an output.");
    }
  }

  public void connect(Bus bus)
  {
    if (bus.getWidth() == connections.size())
    {
      for (int i = 0; i < connections.size(); i++)
      {
        Trace trace = bus.getTrace(i);
        connections.set(i, trace);
      }
    }
    else
    {
      throw new EmulatorException("Cannot connect bus with width [" + bus.getWidth() + "] to omniport with a different width [" + connections.size() + "].");
    }
  }

  public void connect(Trace trace)
  {
    if (connections.size() == 1)
    {
      connections.set(0, trace);
    }
    else
    {
      throw new EmulatorException("Cannot connect bus with width [1] to omniport with a different width [" + connections.size() + "].");
    }
  }

  public void writeAllPinsUndefined()
  {
    for (int i = 0; i < pins.size(); i++)
    {
      pins.set(i, Undefined);
    }
  }

  public void writeAllPinsError()
  {
    for (int i = 0; i < pins.size(); i++)
    {
      pins.set(i, Error);
    }
  }

  public TraceValue readStates()
  {
    if (state == NotSet)
    {
      state = Input;
    }

    if (state == Input)
    {
      boolean high = false;
      boolean low = false;
      boolean error = false;
      boolean undefined = false;
      int length = pins.size();
      for (int i = 0; i < length; i++)
      {
        Trace connection = connections.get(i);
        TraceValue value;
        if (connection != null)
        {
          value = connection.getValue();
          pins.set(i, value);
        }
        else
        {
          value = Undefined;
        }

        if (value.isError())
        {
          error = true;
        }
        else if (value.isUndefined())
        {
          undefined = true;
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

      if (error)
      {
        return Error;
      }
      else if (undefined)
      {
        return Undefined;
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
      else
      {
        return Undefined;
      }
    }
    else
    {
      throw new EmulatorException("Cannot read from a Port that is not an input.");
    }
  }

  public boolean writeState()
  {
    if (state == NotSet)
    {
      state = Output;
    }

    return state == Output;
  }

  public String getStringValue()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (TraceValue traceValue : pins)
    {
      char c = traceValue.getStringValue();
      stringBuilder.insert(0, c);
    }
    return stringBuilder.toString();
  }
}

