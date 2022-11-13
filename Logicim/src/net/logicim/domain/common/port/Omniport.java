package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Bus;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.propagation.OutputVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

import static net.logicim.domain.common.TransmissionState.Output;
import static net.logicim.domain.common.trace.TraceValue.High;
import static net.logicim.domain.common.trace.TraceValue.Low;

public class Omniport
    extends Port
{
  protected TraceNet[] traces;
  protected float[] outputVoltages;
  protected int width;

  public Omniport(PortType type,
                  Pins pins,
                  String name,
                  int width,
                  VoltageConfiguration voltageConfiguration)
  {
    super(type, pins, name, voltageConfiguration);
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
      if (voltageConfiguration.isOutput())
      {
        OutputVoltageConfiguration outputVoltageConfiguration = (OutputVoltageConfiguration) voltageConfiguration;
        if (traces.length == width)
        {
          TraceValue[] traceValues = new TraceValue[traces.length];
          for (int i = 0; i < traces.length; i++)
          {
            TraceValue value = Low;
            if ((longValue >> i & 1) == 1)
            {
              value = High;
            }
            traceValues[i] = value;
            outputVoltageConfiguration.createOutputEvents(simulation.getTimeline(), traceValues, this);
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
    if (width == 1)
    {
      traces[index] = trace;
    }
    else
    {
      throw new SimulatorException("Cannot connect Port [" + getDescription() + "] with width [" + width + "] to Bus with a different width [1].");
    }
  }

  @Override
  public void disconnect()
  {
    for (TraceNet trace : traces)
    {
      trace.disconnect(this);
    }
    traces = null;
    outputVoltages = null;
  }

  @Override
  public boolean isDriven(TraceNet trace)
  {
    for (TraceNet testTrace : traces)
    {
      if (trace == testTrace)
      {
        return outputDriven;
      }
    }
    return false;
  }

  @Override
  public float getDrivenVoltage(TraceNet trace)
  {
    int index = 0;
    if (!outputDriven)
    {
      throw new SimulatorException("Port [" + getName() + "] is not driven.");
    }

    for (TraceNet testTrace : traces)
    {
      if (trace == testTrace)
      {
        return outputVoltages[index];
      }

      index++;
    }
    throw new SimulatorException("Port [" + getName() + "] is not connected to trace.");
  }

  public float getDrivenVoltage(int busIndex)
  {
    if (outputDriven)
    {
      return outputVoltages[busIndex];
    }
    else
    {
      throw new SimulatorException("Port [" + getName() + "] is not driven.");
    }
  }

  public void setOutputVoltages(float[] outputVoltages)
  {
    this.outputVoltages = outputVoltages;
  }
}

