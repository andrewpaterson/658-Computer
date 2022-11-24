package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.linkedlist.LinkedList;
import net.logicim.common.collection.linkedlist.LinkedListIterator;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.event.*;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.trace.TraceValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.logicim.domain.common.trace.TraceValue.Undriven;

public class Port
{
  protected PortType type;
  protected Pins pins;
  protected String name;
  protected VoltageConfiguration voltageConfiguration;

  protected LinkedList<PortEvent> events;
  protected TraceNet trace;
  protected PortOutputEvent output;

  public Port(PortType type,
              Pins pins,
              String name,
              VoltageConfiguration voltageConfiguration)
  {
    this.type = type;
    this.pins = pins;
    this.name = name;
    this.voltageConfiguration = voltageConfiguration;
    events = new LinkedList<>();
    pins.addPort(this);
  }

  public String getName()
  {
    return name;
  }

  public String toDebugString()
  {
    return getPins().getIntegratedCircuit().getName() + "." + name;
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

  public void writeBool(Timeline timeline, boolean value)
  {
    voltageConfiguration.createOutputEvent(timeline, this, voltageConfiguration.getVoltage(value));
  }

  public void highImpedance(Timeline timeline)
  {
    voltageConfiguration.createHighImpedanceEvents(timeline, this);
  }

  public TraceValue readValue(long time)
  {
    if (trace != null)
    {
      float voltage = trace.getVoltage(time);
      if (!Float.isNaN(voltage))
      {
        return voltageConfiguration.getValue(voltage);
      }
    }
    return Undriven;
  }

  public void disconnect(Simulation simulation)
  {
    if (trace != null)
    {
      trace.disconnect(this);
      trace = null;
    }
    List<PortEvent> events = this.events.toList();
    for (PortEvent event : events)
    {
      simulation.removeEvent(event);
    }
    this.events.clear();
  }

  public float getVoltage(long time)
  {
    if (output == null)
    {
      return Float.NaN;
    }

    return output.getVoltage(time);
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
    slewEvent.update(simulation.getTimeline());

    Set<Port> connectedPorts = getConnectedPorts();
    if (connectedPorts != null)
    {
      for (Port connectedPort : connectedPorts)
      {
        if (connectedPort != this)
        {
          connectedPort.traceSlew(simulation, slewEvent);
        }
      }
    }

    setLastOutput(slewEvent);
  }

  public boolean traceSlew(Simulation simulation, SlewEvent slewEvent)
  {
    float startVoltage = slewEvent.getStartVoltage();
    long slewTime = slewEvent.getSlewTime();
    float endVoltage = slewEvent.getEndVoltage();

    if (startVoltage != endVoltage)
    {
      float lowVoltageIn = voltageConfiguration.getLowVoltageIn();
      float highVoltageIn = voltageConfiguration.getHighVoltageIn();
      long transitionTime;
      float transitionVoltage;
      if (endVoltage <= lowVoltageIn)
      {
        transitionVoltage = lowVoltageIn;
        transitionTime = calculateLowTransitionTime(slewEvent, startVoltage, endVoltage, transitionVoltage);
      }
      else if (endVoltage >= highVoltageIn)
      {
        transitionVoltage = highVoltageIn;
        transitionTime = calculateHighTransitionTime(slewEvent, startVoltage, endVoltage, transitionVoltage);
      }
      else
      {
        transitionVoltage = (highVoltageIn + lowVoltageIn) / 2;
        transitionTime = calculateTransitionTime(startVoltage, endVoltage, slewTime, transitionVoltage);
      }

      if (transitionTime > 0)
      {
        new TransitionEvent(this, transitionVoltage, transitionTime, simulation.getTimeline());
        return true;
      }
    }
    return false;
  }

  public void traceConnected(Simulation simulation)
  {
    TraceNet trace = getTrace();
    List<PortOutputEvent> outputEvents = trace.getOutputEvents();
    boolean slewInProgress = false;
    for (PortOutputEvent outputEvent : outputEvents)
    {
      if (outputEvent instanceof SlewEvent)
      {
        SlewEvent slewEvent = (SlewEvent) outputEvent;
        if (slewEvent.forTime(simulation.getTime()))
        {
          slewInProgress |= traceSlew(simulation, slewEvent);
        }
      }
    }

    if (!slewInProgress)
    {
      createTransitionEventFromExistingVoltage(simulation, trace);
    }
  }

  protected void createTransitionEventFromExistingVoltage(Simulation simulation, TraceNet trace)
  {
    float endVoltage = trace.getVoltage(simulation.getTime());
    float lowVoltageIn = voltageConfiguration.getLowVoltageIn();
    float highVoltageIn = voltageConfiguration.getHighVoltageIn();

    float transitionVoltage = 0;
    boolean traceValid = false;
    if (endVoltage <= lowVoltageIn)
    {
      traceValid = true;
      transitionVoltage = lowVoltageIn;
    }
    else if (endVoltage >= highVoltageIn)
    {
      traceValid = true;
      transitionVoltage = highVoltageIn;
    }

    if (traceValid)
    {
      new TransitionEvent(this, transitionVoltage, (long) 1, simulation.getTimeline());
    }
  }

  protected long calculateLowTransitionTime(SlewEvent slewEvent, float startVoltage, float endVoltage, float lowVoltageIn)
  {
    long transitionTime = calculateTransitionTime(startVoltage, endVoltage, slewEvent.getSlewTime(), lowVoltageIn);
    for (; ; )
    {
      float voltage = slewEvent.calculateVoltageAtTime(slewEvent.getTime() + transitionTime, startVoltage);
      if (voltage > lowVoltageIn)
      {
        transitionTime++;
      }
      else
      {
        return transitionTime;
      }
    }
  }

  protected long calculateHighTransitionTime(SlewEvent slewEvent, float startVoltage, float endVoltage, float highVoltageIn)
  {
    long transitionTime = calculateTransitionTime(startVoltage, endVoltage, slewEvent.getSlewTime(), highVoltageIn);
    for (; ; )
    {
      float voltage = slewEvent.calculateVoltageAtTime(slewEvent.getTime() + transitionTime, startVoltage);
      if (voltage < highVoltageIn)
      {
        transitionTime++;
      }
      else
      {
        return transitionTime;
      }
    }
  }

  protected void setLastOutput(PortOutputEvent outputEvent)
  {
    this.output = outputEvent;
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
    float voltage = voltageConfiguration.getMidVoltageOut();
    voltageConfiguration.createOutputEvent(timeline, this, voltage);
  }

  public Set<Port> getConnectedPorts()
  {
    if (trace != null)
    {
      return trace.getConnectedPorts();
    }
    else
    {
      return null;
    }
  }

  public VoltageConfiguration getVoltageConfiguration()
  {
    return voltageConfiguration;
  }

  public List<PortOutputEvent> getOverlappingEvents(long endTime)
  {
    List<PortOutputEvent> result = new ArrayList<>();
    for (PortEvent event : events)
    {
      if (event instanceof PortOutputEvent)
      {
        if (event.getTime() <= endTime)
        {
          result.add((PortOutputEvent) event);
        }
      }
    }
    return result;
  }

  public void reset()
  {
    output = null;
    events.clear();
  }

  public PortOutputEvent getOutput()
  {
    return output;
  }

  public void setOutput(PortOutputEvent output)
  {
    this.output = output;
  }

  public long getTraceId()
  {
    if (trace != null)
    {
      return trace.getId();
    }
    return 0;
  }
}

