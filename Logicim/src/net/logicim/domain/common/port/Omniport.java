package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Bus;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.propagation.OutputVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.trace.TraceNet;

public class Omniport
    extends Port
{
  protected TraceNet[] traces;
  protected Drive[] outputs;
  protected int width;

  public Omniport(PortType type,
                  Pins pins,
                  String name,
                  int width,
                  VoltageConfiguration voltageConfiguration)
  {
    super(type, pins, name, voltageConfiguration);
    this.width = width;
    this.traces = new TraceNet[width];
    this.outputs = new Drive[width];
  }

  public void writeAllPinsBool(Simulation simulation, long longValue)
  {
    if (state.isOutput())
    {
      if (voltageConfiguration.isOutput())
      {
        OutputVoltageConfiguration outputVoltageConfiguration = (OutputVoltageConfiguration) voltageConfiguration;
        if (traces.length == width)
        {
          boolean[] traceValues = new boolean[traces.length];
          for (int i = 0; i < traces.length; i++)
          {
            boolean value = false;
            if ((longValue >> i & 1) == 1)
            {
              value = true;
            }
            traceValues[i] = value;
            outputVoltageConfiguration.createDriveEvents(simulation.getTimeline(), traceValues, this);
          }
        }
      }
      else
      {
        throwNoOutputVoltageConfigurationException();
      }
    }
    else
    {
      throwCannotWriteToPortException();
    }
  }

  public void connect(Bus bus)
  {
    if (bus.getWidth() == width)
    {
      for (int i = 0; i < width; i++)
      {
        TraceNet trace = bus.getTrace(i);
        traces[i] = trace;
      }
    }
    else
    {
      throw new SimulatorException("Cannot connect Port [" + getDescription() + "] with width [" + width + "] to Bus with a different width [" + bus.getWidth() + "].");
    }
  }

  public void connect(int index, TraceNet trace)
  {
    if (index < width)
    {
      traces[index] = trace;
    }
    else
    {
      throw new SimulatorException("Cannot connect Port [" + getDescription() + "] with width [" + width + "] to Bus with a different width [1].");
    }
  }

  @Override
  public void connect(TraceNet trace)
  {
    if (width == 1)
    {
      traces[0] = trace;
    }
    else
    {
      throw new SimulatorException("Cannot connect Port [" + getDescription() + "] with width [" + width + "] to Bus with a different width [1].");
    }

  }

  @Override
  public void disconnect()
  {
    for (int i = 0; i < traces.length; i++)
    {
      TraceNet trace = traces[i];
      trace.disconnect(this);
      traces[i] = null;
      outputs[i] = null;
    }
  }

  @Override
  public boolean isDriven(TraceNet trace)
  {
    for (int i = 0; i < traces.length; i++)
    {
      TraceNet testTrace = traces[i];
      if (trace == testTrace)
      {
        return outputs[i].isDriven();
      }
    }
    return false;
  }

  public boolean isDriven(int busIndex)
  {
    return outputs[busIndex].isDriven();
  }

  @Override
  public Drive getDrive(TraceNet trace)
  {
    for (int i = 0; i < traces.length; i++)
    {
      TraceNet testTrace = traces[i];
      if (trace == testTrace)
      {
        return outputs[i];
      }
    }
    throw new SimulatorException("Port [" + getName() + "] is not connected to trace.");
  }

  public float getDrivenVoltage(int busIndex)
  {
    if (outputs[busIndex].isDriven())
    {
      return outputs[busIndex].voltage;
    }
    else
    {
      throw new SimulatorException("Port [" + getName() + "] is not driven.");
    }
  }

  public void setOutputVoltages(float[] outputVoltages)
  {
    throw new SimulatorException("Not yet implemented.");
  }
}

