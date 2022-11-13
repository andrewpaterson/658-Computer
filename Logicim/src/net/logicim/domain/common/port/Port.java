package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.collection.linkedlist.LinkedListIterator;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.TransmissionState;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.port.event.UniportSlewEvent;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

import static net.logicim.domain.common.TransmissionState.NotSet;

public abstract class Port
{
  protected boolean outputDriven;
  protected PortType type;
  protected Pins pins;
  protected String name;
  protected TransmissionState state;
  protected VoltageConfiguration voltageConfiguration;

  protected LinkedList<PortEvent> events;

  public Port(PortType type,
              Pins pins,
              String name,
              VoltageConfiguration voltageConfiguration)
  {
    this.type = type;
    this.pins = pins;
    this.name = name;
    this.voltageConfiguration = voltageConfiguration;
    this.state = NotSet;
    this.outputDriven = false;
    events = new LinkedList<>();
    pins.addPort(this);
  }

  public String getName()
  {
    return name;
  }

  public String getPortTransmissionStateAsString()
  {
    String portStateString = "  ";
    if (state.isInput())
    {
      portStateString = "<-";
    }
    else if (state.isOutput())
    {
      portStateString = "->";
    }
    else if (state.isNotSet())
    {
      portStateString = "..";
    }
    else if (state.isImpedance())
    {
      portStateString = "xx";
    }
    return getName() + "[" + portStateString + "]";
  }

  public String getDescription()
  {
    return getPins().getDescription() + "." + getName();
  }

  public Pins getPins()
  {
    return pins;
  }

  public boolean isInput()
  {
    return type == PortType.Input ||
           type == PortType.Bidirectional ||
           type == PortType.Passive ||
           type == PortType.PowerIn;
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

  public boolean isDriven()
  {
    return outputDriven;
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

  public abstract void disconnect();

  public abstract boolean isDriven(TraceNet traceNet);

  public abstract float getDrivenVoltage(TraceNet traceNet);
}

