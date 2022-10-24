package net.logicim.domain.common;

import net.logicim.common.SimulatorException;

import java.util.List;

import static net.logicim.domain.common.TransmissionState.Output;

public class Omniport
    extends Port
{
  protected List<Trace> traces;
  protected int width;

  public Omniport(Pins tickable, String name, int width)
  {
    super(tickable, name);
    this.width = width;
  }

  public void writeAllPinsBool(long longValue)
  {
    if (state.isNotSet())
    {
      state = Output;
    }

    if (state.isOutput())
    {
      if (traces.size() == width)
      {
        for (int i = 0; i < traces.size(); i++)
        {
          boolean value = false;
          if ((longValue >> i & 1) == 1)
          {
            value = true;
          }

          float outVoltage = value ? highVoltageOut : 0;
          Trace trace = traces.get(i);
          float traceVoltage = trace.getVoltage();
          if (outVoltage != traceVoltage)
          {
            if (outVoltage > traceVoltage)
            {
              pins.getTimeline().createPropagationEvent(trace, outVoltage, lowToHighPropagationDelay);
            }
            else if (outVoltage < traceVoltage)
            {
              pins.getTimeline().createPropagationEvent(trace, outVoltage, highToLowPropagationDelay);
            }
          }
        }
      }
    }
    else
    {
      throw new SimulatorException("Cannot write to Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  public void connect(Bus bus)
  {
    if (bus.getWidth() == width)
    {
      for (int i = 0; i < width; i++)
      {
        Trace trace = bus.getTrace(i);
        traces.set(i, trace);
      }
    }
    else
    {
      throw new SimulatorException("Cannot connect Port [" + getDescription() + "] with width [" + width + "] to Bus with a different width [" + bus.getWidth() + "].");
    }
  }

  public void connect(Trace trace)
  {
    if (width == 1)
    {
      traces.set(0, trace);
    }
    else
    {
      throw new SimulatorException("Cannot connect Port [" + getDescription() + "] with width [" + width + "] to Bus with a different width [1].");
    }
  }

  @Override
  public List<Trace> getConnections()
  {
    return traces;
  }
}

