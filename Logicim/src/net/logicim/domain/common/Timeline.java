package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.redblacktree.RedBlackTree;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceNet;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.logicim.domain.common.LongTime.timeToNanoseconds;

public class Timeline
{
  protected List<Pins> tickables;
  protected long time;
  protected RedBlackTree<Long, SimultaneousEvents> events;

  public Timeline()
  {
    this.tickables = new ArrayList<>();
    this.events = new RedBlackTree<>();
    time = 0;
  }

  public void add(Pins tickable)
  {
    tickables.add(tickable);
  }

  public Event createPropagationEvent(TraceNet trace, float outVoltage, int propagationDelay)
  {
    long eventTime = this.time + propagationDelay;
    Event event = new Event(eventTime, outVoltage, trace);

    SimultaneousEvents simultaneousEvents = events.find(eventTime);
    if (simultaneousEvents == null)
    {
      simultaneousEvents = new SimultaneousEvents(eventTime);
      events.add(simultaneousEvents);
    }
    simultaneousEvents.add(event);
    if (trace != null)
    {
      trace.add(event);
    }
    return event;
  }

  public void run()
  {
    for (; ; )
    {
      SimultaneousEvents events = this.events.findFirst();
      if (events != null)
      {
        if (events.time > time)
        {
          time = events.time;
        }
        else
        {
          throw new SimulatorException("Cannot update simulation time.  Event time [" + timeToNanoseconds(events.time) + "] must be after current time [" + timeToNanoseconds(time) + "].");
        }

        Map<IntegratedCircuit<? extends Pins>, List<Port>> integratedCircuits = new LinkedHashMap<>();
        for (Event event : events.events)
        {
          TraceNet trace = event.getTrace();
          trace.update(event.getVoltage());

          List<Port> ports = trace.getInputPorts();
          for (Port port : ports)
          {
            IntegratedCircuit<? extends Pins> integratedCircuit = port.getPins().getIntegratedCircuit();
            List<Port> inputPorts = integratedCircuits.get(integratedCircuit);
            if (inputPorts == null)
            {
              inputPorts = new ArrayList<>();
              integratedCircuits.put(integratedCircuit, inputPorts);
            }
            inputPorts.add(port);
          }
          trace.remove(event);
        }

        for (Map.Entry<IntegratedCircuit<? extends Pins>, List<Port>> integratedCircuitListEntry : integratedCircuits.entrySet())
        {
          IntegratedCircuit<? extends Pins> integratedCircuit = integratedCircuitListEntry.getKey();
          List<Port> ports = integratedCircuitListEntry.getValue();
          integratedCircuit.tick(time, ports);
        }

        this.events.remove(events);
        events.done();
      }
      else
      {
        break;
      }
    }
  }

  public long getTime()
  {
    return time;
  }
}

