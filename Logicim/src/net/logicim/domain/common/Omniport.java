package net.logicim.domain.common;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.TraceValue.*;
import static net.logicim.domain.common.TransmissionState.*;

public class Omniport
    extends Port
{
  protected List<TraceValue> pins;
  protected List<Trace> wires;

  public Omniport(TickablePins tickable, String name, int width)
  {
    super(tickable, name);
    wires = new ArrayList<>();
    pins = new ArrayList<>(width);

    for (int i = 0; i < width; i++)
    {
      pins.add(Unsettled);
      wires.add(null);
    }
  }

  public void resetConnections()
  {
    super.resetConnections();

    int length = pins.size();
    for (int i = 0; i < length; i++)
    {
      pins.set(i, Unsettled);
      Trace trace = wires.get(i);
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
        Trace connection = wires.get(i);
        if (connection != null)
        {
          value = connection.updateNetValue(value, this);
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
        TraceValue pin = pins.get(i);
        value <<= 1;
        if (pin.isHigh())
        {
          value |= 1;
        }
        else if (pin.isInvalid())
        {
          throw new EmulatorException("Cannot read a boolean value from Port [" + getDescription() + "] that has an invalid value [" + pin.toEnumString() + "].");
        }
      }
      return value;
    }
    else
    {
      throw new EmulatorException("Cannot read from Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  public void writeAllPinsBool(long longValue)
  {
    if (state.isNotSet())
    {
      state = Output;
    }

    if (state.isOutput())
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
      throw new EmulatorException("Cannot write to Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  public void connect(Bus bus)
  {
    if (bus.getWidth() == wires.size())
    {
      for (int i = 0; i < wires.size(); i++)
      {
        Trace trace = bus.getTrace(i);
        wires.set(i, trace);
      }
    }
    else
    {
      throw new EmulatorException("Cannot connect Port [" + getDescription() + "] with width [" + wires.size() + "] to Bus with a different width [" + bus.getWidth() + "].");
    }
  }

  public void connect(Trace trace)
  {
    if (wires.size() == 1)
    {
      wires.set(0, trace);
    }
    else
    {
      throw new EmulatorException("Cannot connect Port [" + getDescription() + "] with width [" + wires.size() + "] to Bus with a different width [1].");
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
        Trace connection = wires.get(i);
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
      throw new EmulatorException("Cannot read from Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  public void highImpedance()
  {
    if (state.isNotSet())
    {
      state = Impedance;
    }

    if (!state.isImpedance())
    {
      throw new EmulatorException("Cannot high-impedance Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  @Override
  public List<Trace> getConnections()
  {
    return wires;
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
  public String getWireValuesAsString()
  {
    boolean unsettled = false;
    boolean error = false;
    boolean notConnected = false;
    StringBuilder stringBuilder = new StringBuilder();

    long value = 0;

    for (int i = wires.size() - 1; i >= 0; i--)
    {
      Trace wire = wires.get(i);
      value <<= 1;
      if (wire.isHigh())
      {
        value |= 1;
      }
      else if (wire.isUnsettled())
      {
        unsettled = true;
      }
      else if (wire.isError())
      {
        error = true;
      }
      else if (wire.isNotConnected())
      {
        notConnected = true;
      }

      if (i % 4 == 0)
      {
        char e;
        if (error)
        {
          e = 'E';
        }
        else if (unsettled)
        {
          e = '.';
        }
        else if (notConnected)
        {
          e = ' ';
        }
        else
        {
          e = Long.toHexString(value).charAt(0);
        }
        stringBuilder.append(e);
        value = 0;
        unsettled = false;
        error = false;
        notConnected = false;
      }
    }
    return stringBuilder.toString();
  }

  @Override
  public String getConnectionValuesAsString()
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (Trace trace : wires)
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

