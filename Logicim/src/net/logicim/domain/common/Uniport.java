package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.propagation.InputPropagation;
import net.logicim.domain.common.propagation.OutputPropagation;
import net.logicim.domain.common.propagation.Propagation;
import net.logicim.domain.common.trace.Trace;
import net.logicim.domain.common.trace.TraceValue;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.TransmissionState.*;

public class Uniport
    extends Port
{
  protected Trace trace;

  public Uniport(Pins pins, String name, Propagation propagation)
  {
    super(pins, name, propagation);
  }

  @Override
  public List<Trace> getConnections()
  {
    ArrayList<Trace> connections = new ArrayList<>();
    connections.add(trace);
    return connections;
  }

  public void writeBool(boolean value)
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
        outputPropagation.createPropagationEvent(value, trace);
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

  public void connect(Trace trace)
  {
    this.trace = trace;
  }

  public void highImpedance()
  {
    if (state.isNotSet())
    {
      state = Impedance;
    }

    if (state.isImpedance())
    {

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
      if (trace.isNotConnected())
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
}

