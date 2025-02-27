package net.logicim.domain.common.port;

import net.common.SimulatorException;
import net.common.collection.linkedlist.LinkedList;
import net.common.collection.linkedlist.LinkedListIterator;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.LongTime;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.event.*;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.common.wire.TraceValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.logicim.domain.common.wire.TraceValue.Undriven;

public class LogicPort
    extends Port
{
  protected VoltageConfigurationSource voltageConfigurationSource;

  protected LinkedList<PortEvent> events;
  protected PortOutputEvent output;

  public LogicPort(PortType type,
                   Pins pins,
                   String name,
                   VoltageConfigurationSource voltageConfigurationSource)
  {
    super(type, name, pins);
    this.voltageConfigurationSource = voltageConfigurationSource;
    this.events = new LinkedList<>();
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
    events.remove(event);
  }

  public LinkedList<PortEvent> getEvents()
  {
    return events;
  }

  public void writeBool(Timeline timeline, boolean value)
  {
    long time = timeline.getTime();
    float vcc = getVCC(time);

    float outVoltage = voltageConfigurationSource.getVoltageOut(value, vcc);
    long holdTime = voltageConfigurationSource.calculateHoldTime(outVoltage, getVoltageOut(time), vcc);
    if (holdTime != Long.MAX_VALUE)
    {
      new SlewEvent(this, outVoltage, holdTime, timeline);
    }
  }

  public void writeImpedance(Timeline timeline)
  {
    new DriveEvent(this, LongTime.nanosecondsToTime(0.01), Float.NaN, timeline);
  }

  public TraceValue readValue(long time)
  {
    if (trace != null)
    {
      float voltage = trace.getVoltage(time);
      if (!Float.isNaN(voltage))
      {
        return voltageConfigurationSource.getValue(voltage, getVCC(time));
      }
    }
    return Undriven;
  }

  public void disconnect(Simulation simulation)
  {
    super.disconnect(simulation);

    List<PortEvent> events = this.events.toList();
    for (PortEvent event : events)
    {
      simulation.removeEvent(event);
    }
    this.events.clear();
  }

  public float getVoltageOut(long time)
  {
    if (output == null)
    {
      return Float.NaN;
    }

    return output.getVoltage(time);
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
          if (connectedPort.isLogicPort())
          {
            ((LogicPort) connectedPort).traceSlew(simulation, slewEvent);
          }
        }
      }
    }

    setLastOutput(slewEvent);
  }

  public boolean traceSlew(Simulation simulation, SlewEvent slewEvent)
  {
    if (voltageConfigurationSource != null)
    {
      float startVoltage = slewEvent.getStartVoltage();
      long slewTime = slewEvent.getSlewTime();
      float endVoltage = slewEvent.getEndVoltage();

      if (startVoltage != endVoltage)
      {
        long time = simulation.getTime();

        float lowVoltageIn = voltageConfigurationSource.getLowVoltageIn(getVCC(time));
        float highVoltageIn = voltageConfigurationSource.getHighVoltageIn(getVCC(time));
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
    }
    return false;
  }

  public void traceConnected(Simulation simulation)
  {
    super.traceConnected(simulation);

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

  protected void createTransitionEventFromExistingVoltage(Simulation simulation, Trace trace)
  {
    if (voltageConfigurationSource != null)
    {
      long time = simulation.getTime();

      float endVoltage = trace.getVoltage(time);
      float lowVoltageIn = voltageConfigurationSource.getLowVoltageIn(getVCC(time));
      float highVoltageIn = voltageConfigurationSource.getHighVoltageIn(getVCC(time));

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
        new TransitionEvent(this, transitionVoltage, 1, simulation.getTimeline());
      }
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

  public VoltageConfigurationSource getVoltageConfigurationSource()
  {
    return voltageConfigurationSource;
  }

  public List<DriveEvent> getFutureDriveEvents(long time)
  {
    List<DriveEvent> result = new ArrayList<>();
    for (PortEvent event : events)
    {
      if (event instanceof DriveEvent)
      {
        if (event.getTime() >= time)
        {
          result.add((DriveEvent) event);
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

  public String toEventsString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("Events [" + events.size() + "]:  ");
    for (PortEvent event : events)
    {
      builder.append(event.toShortString());
      builder.append(",  ");
    }
    return builder.toString();
  }

  public float getVCC(long time)
  {
    return getPins().getVoltageCommon().getVoltageIn(time);
  }

  public float getGND(long time)
  {
    return getPins().getVoltageGround().getVoltageIn(time);
  }

  @Override
  public boolean isLogicPort()
  {
    return true;
  }

  public Pins getPins()
  {
    return (Pins) holder;
  }
}

