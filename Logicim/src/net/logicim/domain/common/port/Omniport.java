package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Bus;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.propagation.OutputPropagation;
import net.logicim.domain.common.propagation.Propagation;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

import java.util.List;

import static net.logicim.domain.common.TransmissionState.Output;
import static net.logicim.domain.common.trace.TraceValue.High;
import static net.logicim.domain.common.trace.TraceValue.Low;

public class Omniport
    extends Port
{
  protected List<TraceNet> traces;
  protected int width;

  public Omniport(PortType type,
                  Pins pins,
                  String name,
                  int width,
                  Propagation propagation)
  {
    super(type, pins, name, propagation);
    this.width = width;
  }

  public void writeAllPinsBool(Simulation simulation, long longValue)
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
            TraceValue value = Low;
            if ((longValue >> i & 1) == 1)
            {
              value = High;
            }
            TraceNet trace = traces.get(i);
            outputPropagation.createPropagationEvent(simulation.getTimeline(), value, trace);
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
        TraceNet trace = bus.getTrace(i);
        traces.set(i, trace);
      }
    }
    else
    {
      throw new SimulatorException("Cannot connect Port [" + getDescription() + "] with width [" + width + "] to Bus with a different width [" + bus.getWidth() + "].");
    }
  }

  public void connect(TraceNet trace)
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
}

