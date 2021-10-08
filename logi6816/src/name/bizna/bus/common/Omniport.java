package name.bizna.bus.common;

import name.bizna.util.EmulatorException;

import java.util.ArrayList;
import java.util.List;

public class Omniport
    extends Connector
{
  protected TransmissionState state;
  protected List<TraceState> pins;

  public Omniport(int width)
  {
    state = TransmissionState.Impedance;
    pins = new ArrayList<>(width);
    for (int i = 0; i < width; i++)
    {
      pins.add(TraceState.Undefined);
    }
  }

  public void set(int pin, TraceState value)
  {
    if (pin >= 0 && pin < pins.size())
    {
      if (state == TransmissionState.Output)
      {
        this.pins.set(pin, value);
      }
      else
      {
        this.pins.set(pin, TraceState.Error);
      }
    }
    else
    {
      throw new EmulatorException("Pin exceeds allowed bus width.");
    }
  }

  public void set(long longValue)
  {
    for (int i = 0; i < pins.size(); i++)
    {
      TraceState value = TraceState.Low;
      if ((longValue >> i & 1) == 1)
      {
        value = TraceState.High;
      }
      set(i, value);
    }
  }

  public TraceState get(int pin)
  {
    if (pin >= 0 && pin < pins.size())
    {
      return pins.get(pin);
    }
    else
    {
      throw new EmulatorException("Pin exceeds allowed bus width.");
    }
  }

  public long get()
  {
    long value = 0;
    for (TraceState traceState : pins)
    {
      value <<= 1;
      if (traceState == TraceState.High)
      {
        value |= 1;
      }
    }
    return value;
  }
}

