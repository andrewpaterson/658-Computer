package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.propagation.OutputPropagation;
import net.logicim.domain.common.propagation.Propagation;
import net.logicim.domain.common.trace.Trace;

import java.util.List;

import static net.logicim.domain.common.TransmissionState.Output;

public class Omniport
    extends Port
{
  protected List<Trace> traces;
  protected int width;

  public Omniport(Pins tickable, String name, int width, Propagation propagation)
  {
    super(tickable, name, propagation);
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
      if (propagation.isOutput())
      {
        OutputPropagation outputPropagation = (OutputPropagation) propagation;
        if (traces.size() == width)
        {
          for (int i = 0; i < traces.size(); i++)
          {
            boolean value = false;
            if ((longValue >> i & 1) == 1)
            {
              value = true;
            }
            Trace trace = traces.get(i);
            outputPropagation.createPropagationEvent(value, trace);
          }
        }
      }
      else
      {
        throw new SimulatorException("Cannot write an output value for port [" + getDescription() + "] without an output propagation configured.");
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

