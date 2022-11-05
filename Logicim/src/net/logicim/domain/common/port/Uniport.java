package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.propagation.InputPropagation;
import net.logicim.domain.common.propagation.OutputPropagation;
import net.logicim.domain.common.propagation.Propagation;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

import static net.logicim.domain.common.TransmissionState.*;
import static net.logicim.domain.common.trace.TraceValue.Undriven;

public class Uniport
    extends Port
{
  protected TraceNet trace;

  public Uniport(PortType type,
                 Pins pins,
                 String name,
                 Propagation propagation)
  {
    super(type, pins, name, propagation);
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
    if (state.isNotSet())
    {
      state = Output;
    }

    if (state.isOutput())
    {
      if (propagation.isOutput())
      {
        if (trace != null)
        {
          OutputPropagation outputPropagation = (OutputPropagation) propagation;
          outputPropagation.createPropagationEvent(timeline, TraceValue.getOutputValue(value), trace);
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

  public void writeUnsettled()
  {

  }

  public void highImpedance(Timeline timeline)
  {
    if (state.isNotSet())
    {
      state = Impedance;
    }

    if (state.isImpedance())
    {
      if (propagation.isOutput())
      {
        OutputPropagation outputPropagation = (OutputPropagation) propagation;
        outputPropagation.createPropagationEvent(timeline, Undriven, trace);
      }
      else
      {
        throw new SimulatorException("Cannot write an output value for port [" + getDescription() + "] without an output propagation configured.");
      }
    }
    else
    {
      throw new SimulatorException("Cannot high-impedance Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  public TraceValue readValue()
  {
    if (state.isNotSet())
    {
      state = Input;
    }

    if (state.isInput())
    {
      if (propagation.isInput())
      {
        return ((InputPropagation) propagation).getValue(trace.getVoltage());
      }
      else
      {
        throw new SimulatorException("Cannot read an input value for port [" + getDescription() + "] without an input propagation configured.");
      }
    }
    else
    {
      throw new SimulatorException("Cannot read from Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
    }
  }

  public String getWireValuesAsString()
  {
    if (trace != null)
    {
      if (trace.isUndriven())
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

  public float getVoltage()
  {
    if (trace != null)
    {
      return trace.getVoltage();
    }
    else
    {
      return TraceNet.Undriven;
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

  public TraceNet getTrace()
  {
    return trace;
  }
}

