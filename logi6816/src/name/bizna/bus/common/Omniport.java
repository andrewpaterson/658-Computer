package name.bizna.bus.common;

import name.bizna.bus.gate.Tickable;
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
      pins.add(Unsettled);
      connections.add(null);
    }
  }

  public void resetConnections()
  {
    super.resetConnections();

    int length = pins.size();
    for (int i = 0; i < length; i++)
    {
      pins.set(i, Unsettled);
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
    if (state.isOutput())
    {
      int length = pins.size();
      for (int i = 0; i < length; i++)
      {
        TraceValue value = pins.get(i);
        Trace connection = connections.get(i);
        if (connection != null)
        {
          value = connection.updateNetValue(value);
          pins.set(i, value);
        }
      }
    }
  }

  public long getPinsAsBoolAfterRead()
  {
    if (state.isInput())
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

  public void writeAllPinsBool(long longValue)
  {
    if (write())
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

  public void unset()
  {
    state = NotSet;
    for (int i = 0; i < pins.size(); i++)
    {
      pins.set(i, Unsettled);
    }
  }

  public void error()
  {
    state = NotSet;
    for (int i = 0; i < pins.size(); i++)
    {
      pins.set(i, Error);
    }
  }

  public TraceValue read()
  {
    if (state.isNotSet())
    {
      state = Input;
    }

    if (state.isInput())
    {
      boolean high = false;
      boolean low = false;
      boolean error = false;
      boolean unsettled = false;
      boolean connected = false;

      int length = pins.size();
      for (int i = 0; i < length; i++)
      {
        Trace connection = connections.get(i);
        TraceValue value;
        if (connection != null)
        {
          value = connection.getValue();
          pins.set(i, value);
          connected = true;
        }
        else
        {
          value = NotConnected;
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
    else
    {
      throw new EmulatorException("Cannot read from a Port that is not an input.");
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

  public boolean highImpedance()
  {
    if (state.isNotSet())
    {
      state = Impedance;
    }

    return state.isImpedance();
  }

  @Override
  public List<Trace> getConnections()
  {
    return connections;
  }

  @Override
  public String getTraceValuesAsString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (TraceValue traceValue : pins)
    {
      char c = traceValue.getStringValue();
      stringBuilder.insert(0, c);
    }
    return stringBuilder.toString();
  }

  @Override
  public String getConnectionValuesAsString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (Trace trace : connections)
    {
      char c;
      if (trace != null)
      {
        c = trace.getStringValue();
      }
      else
      {
        c = ' ';
      }

      stringBuilder.insert(0, c);
    }
    return stringBuilder.toString();
  }
}

