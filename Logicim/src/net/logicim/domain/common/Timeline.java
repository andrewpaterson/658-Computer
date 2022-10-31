package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.redblacktree.RedBlackTree;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.trace.TraceNet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.logicim.domain.common.LongTime.timeToNanoseconds;

public class Timeline
{
  protected long time;
  protected long previousEventTime;
  protected long eventTime;
  protected RedBlackTree<Long, SimultaneousEvents> events;
  protected Simulation simulation;

  public Timeline(Simulation simulation)
  {
    this.simulation = simulation;
    this.events = new RedBlackTree<>();
    time = 0;
    previousEventTime = 0;
    eventTime = 0;
  }

  public TraceEvent createPropagationEvent(TraceNet trace, float outVoltage, long propagationDelay)
  {
    TraceEvent event = new TraceEvent(this.time + propagationDelay, outVoltage, trace);
    addEvent(event);

    if (trace != null)
    {
      trace.add(event);
    }
    return event;
  }

  public ClockEvent createClockEvent(long propagationDelay, IntegratedCircuit<?, ?> integratedCircuit)
  {
    ClockEvent event = new ClockEvent(this.time + propagationDelay, integratedCircuit);
    addEvent(event);

    return event;
  }

  private void addEvent(Event event)
  {
    long eventTime = event.getTime();
    SimultaneousEvents simultaneousEvents = events.find(eventTime);
    if (simultaneousEvents == null)
    {
      simultaneousEvents = new SimultaneousEvents(eventTime);
      events.add(simultaneousEvents);
    }
    simultaneousEvents.add(event);
  }

  public void run()
  {
    for (; ; )
    {
      if (!runSimultaneous())
      {
        break;
      }
    }
  }

  public boolean runToTime(long timeForward)
  {
    long targetTime = time + timeForward;

    SimultaneousEvents events = this.events.findFirst();
    if (events != null)
    {
      if (events.time >= time)
      {
        if (events.time < targetTime)
        {
          time = events.time;
          runEvent(events);
        }
        else
        {
          time = targetTime;
        }
        return true;
      }
      else
      {
        throw new SimulatorException("Cannot update simulation time.  Event time [" + timeToNanoseconds(events.time) + "] must be after current time [" + timeToNanoseconds(time) + "].");
      }
    }
    else
    {
      return false;
    }
  }

  public boolean runSimultaneous()
  {
    SimultaneousEvents events = this.events.findFirst();
    if (events != null)
    {
      if (events.time >= time)
      {
        time = events.time;
        runEvent(events);
        return true;
      }
      else
      {
        throw new SimulatorException("Cannot update simulation time.  Event time [" + timeToNanoseconds(events.time) + "] must be after current time [" + timeToNanoseconds(time) + "].");
      }
    }
    else
    {
      return false;
    }
  }

  private void runEvent(SimultaneousEvents events)
  {
    previousEventTime = eventTime;
    eventTime = time;

    Map<IntegratedCircuit<? extends Pins, ? extends State>, List<Port>> integratedCircuits = new LinkedHashMap<>();
    for (Event event : events.events)
    {
      if (event instanceof TraceEvent)
      {
        TraceEvent traceEvent = (TraceEvent) event;
        TraceNet trace = traceEvent.getTrace();
        trace.update(traceEvent.getVoltage());

        List<Port> ports = trace.getInputPorts();
        for (Port port : ports)
        {
          IntegratedCircuit<?, ?> integratedCircuit = port.getPins().getIntegratedCircuit();
          List<Port> inputPorts = integratedCircuits.get(integratedCircuit);
          if (inputPorts == null)
          {
            inputPorts = new ArrayList<>();
            integratedCircuits.put(integratedCircuit, inputPorts);
          }
          inputPorts.add(port);
        }
        trace.remove(traceEvent);
      }
      else if (event instanceof ClockEvent)
      {
        ClockEvent clockEvent = (ClockEvent) event;
        IntegratedCircuit<?, ?> integratedCircuit = clockEvent.getIntegratedCircuit();
        integratedCircuit.clockChanged(simulation);
      }
    }

    for (Map.Entry<IntegratedCircuit<? extends Pins, ? extends State>, List<Port>> integratedCircuitListEntry : integratedCircuits.entrySet())
    {
      IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitListEntry.getKey();
      List<Port> ports = integratedCircuitListEntry.getValue();
      integratedCircuit.inputTraceChanged(simulation, ports);
    }

    this.events.remove(events);
    events.done();
  }

  public long getTime()
  {
    return time;
  }
}

