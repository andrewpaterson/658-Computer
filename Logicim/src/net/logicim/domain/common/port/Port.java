package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.collection.linkedlist.LinkedListIterator;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.TransmissionState;
import net.logicim.domain.common.Voltage;
import net.logicim.domain.common.port.event.*;
import net.logicim.domain.common.propagation.InputVoltage;
import net.logicim.domain.common.propagation.OutputVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

import java.util.Set;

import static net.logicim.domain.common.TransmissionState.*;
import static net.logicim.domain.common.trace.TraceValue.Undriven;

public class Port
{
  protected PortType type;
  protected Pins pins;
  protected String name;
  protected TransmissionState state;
  protected VoltageConfiguration voltageConfiguration;

  protected LinkedList<PortEvent> events;
  protected TraceNet trace;
  protected PortOutputEvent lastOutputEvent;

  public Port(PortType type,
              Pins pins,
              String name,
              VoltageConfiguration voltageConfiguration)
  {
    this.type = type;
    this.pins = pins;
    this.name = name;
    this.voltageConfiguration = voltageConfiguration;
    this.state = stateFromType(type);
    events = new LinkedList<>();
    pins.addPort(this);
  }

  private TransmissionState stateFromType(PortType type)
  {
    switch (type)
    {
      case Input:
        return TransmissionState.Input;
      case Output:
        return TransmissionState.Output;
      default:
        return null;
    }
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return getPins().getDescription() + "." + getName();
  }

  public Pins getPins()
  {
    return pins;
  }

  public void add(PortEvent event)
  {
    LinkedListIterator<PortEvent> iterator = events.iterator();
    boolean added = false;
    while (iterator.hasNext())
    {
      PortEvent existingEvent = iterator.next();
      if (existingEvent.getTime() > event.getTime())
      {
        added = true;
        iterator.insertBefore(event);
        break;
      }
    }
    if (!added)
    {
      events.add(event);
    }
  }

  public void remove(PortEvent event)
  {
    boolean removed = events.remove(event);
    if (!removed)
    {
      throw new SimulatorException("Cannot remove event");
    }
  }

  public LinkedList<PortEvent> getEvents()
  {
    return events;
  }

  protected void throwNoOutputVoltageConfigurationException()
  {
    throw new SimulatorException("Cannot write an output value for port [" + getDescription() + "] without a voltage configuration.");
  }

  protected void throwNoInputVoltageConfigurationException()
  {
    throw new SimulatorException("Cannot read an input value for port [" + getDescription() + "] without an input voltage configuration.");
  }

  protected void throwCannotWriteToPortException()
  {
    throw new SimulatorException("Cannot write to Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
  }

  protected void throwCannotReadFromPortException()
  {
    throw new SimulatorException("Cannot read from Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
  }

  protected void throwCannotHighImpedancePortException()
  {
    throw new SimulatorException("Cannot high-impedance Port [" + getDescription() + "] in state [" + state.toEnumString() + "].");
  }

  public void writeBool(Timeline timeline, boolean value)
  {
    state = Output;

    if (state.isOutput())
    {
      if (voltageConfiguration.isOutput())
      {
        OutputVoltageConfiguration outputVoltageConfiguration = (OutputVoltageConfiguration) voltageConfiguration;
        outputVoltageConfiguration.createOutputEvents(timeline, value, this);
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

  public TraceValue readValue(long time)
  {
    state = Input;

    if (state.isInput())
    {
      if (voltageConfiguration.isInput())
      {
        if (trace != null)
        {
          float voltage = trace.getVoltage(time);
          if (!Float.isNaN(voltage))
          {
            return ((InputVoltage) voltageConfiguration).getValue(voltage);
          }
        }
        return Undriven;
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

  public void disconnect()
  {
    if (trace != null)
    {
      trace.disconnect(this);
      trace = null;
    }
  }

  public float getVoltage(long time)
  {
    if (lastOutputEvent == null)
    {
      return Float.NaN;
    }

    return lastOutputEvent.getVoltage(time);
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

  public TraceNet getTrace()
  {
    return trace;
  }

  public void driveEvent(Simulation simulation, DriveEvent driveEvent)
  {
    setLastOutput(driveEvent);
  }

  public void slewEvent(Simulation simulation, SlewEvent slewEvent)
  {
    setLastOutput(slewEvent);

    Set<Port> connectedPorts = getConnectedPorts();
    for (Port connectedPort : connectedPorts)
    {
      if (connectedPort != this)
      {
        connectedPort.traceSlew(simulation, slewEvent);
      }
    }
  }

  public void traceSlew(Simulation simulation, SlewEvent slewEvent)
  {
    InputVoltage inputVoltage = (InputVoltage) voltageConfiguration;
    float startVoltage = slewEvent.getStartVoltage();
    long slewTime = slewEvent.getSlewTime();
    float endVoltage = slewEvent.getEndVoltage();

    if (startVoltage != endVoltage)
    {
      float lowVoltageIn = inputVoltage.getLowVoltageIn();
      float highVoltageIn = inputVoltage.getHighVoltageIn();
      float transitionVoltage;
      if (endVoltage < lowVoltageIn)
      {
        transitionVoltage = lowVoltageIn;
      }
      else if (endVoltage > highVoltageIn)
      {
        transitionVoltage = highVoltageIn;
      }
      else
      {
        throw new SimulatorException("Cannot get transition voltage for end voltage [" + Voltage.getVoltageString(endVoltage) + "].");
      }

      long transitionTime = calculateTransitionTime(startVoltage, endVoltage, slewTime, transitionVoltage);

      simulation.getTimeline().createPortTransitionEvent(this, transitionTime, endVoltage);
    }
  }

  protected void setLastOutput(PortOutputEvent outputEvent)
  {
    this.lastOutputEvent = outputEvent;
    if (outputEvent.getPort() != this)
    {
      throw new SimulatorException("Event was not executed on this port.");
    }
  }

  public void transitionEvent(Simulation simulation, TransitionEvent event)
  {
    getPins().inputTransition(simulation, this);
  }

  protected long calculateTransitionTime(float startVoltage, float endVoltage, long slewTime, float y)
  {
    float c = startVoltage;
    float m = (endVoltage - startVoltage) / slewTime;
    return Math.round((y - c) / m);
  }

  public void writeUnsettled(Timeline timeline)
  {
    if (state.isOutput())
    {
      if (voltageConfiguration.isOutput())
      {
        OutputVoltageConfiguration outputVoltageConfiguration = (OutputVoltageConfiguration) voltageConfiguration;
        outputVoltageConfiguration.createOutputEvents(timeline, value, this);
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

  public Set<Port> getConnectedPorts()
  {
    return trace.getConnectedPorts();
  }
}

