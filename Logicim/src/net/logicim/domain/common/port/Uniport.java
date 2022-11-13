package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.propagation.InputVoltage;
import net.logicim.domain.common.propagation.OutputVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

import static net.logicim.domain.common.TransmissionState.*;
import static net.logicim.domain.common.trace.TraceValue.Undriven;

public class Uniport
    extends Port
{
  protected TraceNet trace;
  protected Drive output;

  public Uniport(PortType type,
                 Pins pins,
                 String name,
                 VoltageConfiguration voltageConfiguration)
  {
    super(type, pins, name, voltageConfiguration);
    output = new Drive();
  }

  public void connect(TraceNet trace)
  {
    if (this.trace == null)
    {
      this.trace = trace;
      trace.connect(this);
    }
    else
    {
      throw new SimulatorException("Port [" + getName() + "] is already connected.");
    }
  }

  public void writeBool(Timeline timeline, boolean value)
  {
    state = Output;

    if (state.isOutput())
    {
      if (voltageConfiguration.isOutput())
      {
        OutputVoltageConfiguration outputVoltageConfiguration = (OutputVoltageConfiguration) voltageConfiguration;
        outputVoltageConfiguration.createDriveEvents(timeline, value, this);
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

  public void highImpedance(Timeline timeline)
  {
    state = Impedance;

    if (state.isImpedance())
    {
      if (voltageConfiguration.isOutput())
      {
        OutputVoltageConfiguration outputVoltageConfiguration = (OutputVoltageConfiguration) voltageConfiguration;
        outputVoltageConfiguration.createHighImpedanceEvents(timeline, this);
      }
      else
      {
        throwNoOutputVoltageConfigurationException();
      }
    }
    else
    {
      throwCannotHighImpedancePortException();
    }
  }

  public TraceValue readValue()
  {
    state = Input;

    if (state.isInput())
    {
      if (voltageConfiguration.isInput())
      {
        if (trace.isDriven())
        {
          return ((InputVoltage) voltageConfiguration).getValue(trace.getDrivenVoltage());
        }
        else
        {
          return Undriven;
        }
      }
      else
      {
        throwNoInputVoltageConfigurationException();
      }
    }
    else
    {
      throwCannotReadFromPortException();
    }
    return null;
  }

  public String getWireValuesAsString()
  {
    if (trace != null)
    {
      if (!trace.isDriven())
      {
        return " ";
      }
      else
      {
        return "" + trace.toString();
      }
    }
    else
    {
      return " ";
    }
  }

  @Override
  public void disconnect()
  {
    if (trace != null)
    {
      trace.disconnect(this);
      trace = null;
    }
  }

  @Override
  public boolean isDriven(TraceNet trace)
  {
    if (trace == this.trace)
    {
      return output.isDriven();
    }
    return false;
  }

  public boolean isDriven()
  {
    return output.isDriven();
  }

  public float getDrivenVoltage()
  {
    if (isDriven())
    {
      return output.getVoltage();
    }
    else
    {
      throw new SimulatorException("Port [" + getName() + "] is not driven.");
    }
  }

  @Override
  public Drive getDrive(TraceNet trace)
  {
    if (trace == this.trace)
    {
      return output;
    }
    else
    {
      throw new SimulatorException("Port [" + getName() + "] is not connected to trace.");
    }
  }

  public TraceNet getTrace()
  {
    return trace;
  }

  public void voltageDriven(Simulation simulation, float voltage)
  {
    output.driveVoltage(voltage);
  }

  public void voltageChanging(Simulation simulation, float startVoltage, float endVoltage, long slewTime)
  {
    if (voltageConfiguration.isInput())
    {
      InputVoltage inputVoltage = (InputVoltage) voltageConfiguration;

      if (startVoltage != endVoltage)
      {
        long transitionTime = calculateTransitionTime(startVoltage, endVoltage, slewTime, inputVoltage.getLowVoltageIn());
        simulation.getTimeline().createPortTransitionEvent(this, transitionTime, endVoltage);
      }
    }
  }

  public void voltageTransition(Simulation simulation, float endVoltage)
  {
    getPins().inputTransition(simulation, this);
  }

  private long calculateTransitionTime(float startVoltage, float endVoltage, long slewTime, float y)
  {
    float c = startVoltage;
    float m = (endVoltage - startVoltage) / slewTime;
    return Math.round((y - c) / m);
  }

  public void writeUnsettled(Timeline timeline)
  {
    throw new SimulatorException("Not yet implemented.");
  }
}

