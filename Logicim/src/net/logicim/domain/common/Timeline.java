package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.redblacktree.RedBlackNode;
import net.logicim.common.collection.redblacktree.RedBlackTree;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.event.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
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

  public SlewEvent createPortSlewEvent(Port port, long holdTime, long slewTime, float startVoltage, float endVoltage)
  {
    SlewEvent event = new SlewEvent(port, startVoltage, endVoltage, slewTime, time + holdTime);
    addEvent(event);
    return event;
  }

  public DriveEvent createPortDriveEvent(Port port, SlewEvent slewEvent)
  {
    DriveEvent event = new DriveEvent(port, slewEvent);
    addEvent(event);
    return event;
  }

  public TransitionEvent createPortTransitionEvent(Port port, long transitionTime, float voltage)
  {
    TransitionEvent event = new TransitionEvent(port, time + transitionTime, voltage);
    addEvent(event);
    return event;
  }

  public TickEvent createTickEvent(long propagationDelay, IntegratedCircuit<?, ?> integratedCircuit)
  {
    TickEvent event = new TickEvent(this.time + propagationDelay, integratedCircuit);
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

    for (; ; )
    {
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
            return true;
          }
        }
        else
        {
          throw new SimulatorException("Cannot update simulation time.  Event time [" + timeToNanoseconds(events.time) + "] must be after current time [" + timeToNanoseconds(time) + "].");
        }
      }
      else
      {
        time = targetTime;
        return false;
      }
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

    for (Event event : events.events)
    {
      event.execute(simulation);
      event.removeFromOwner();
    }

    this.events.remove(events);
    events.done();
  }

  public Map<Long, SimultaneousEvents> getAllEvents()
  {
    Map<Long, SimultaneousEvents> eventsMap = new LinkedHashMap<>();
    Iterator<RedBlackNode<Long, SimultaneousEvents>> iterator = events.nodeIterator();
    while (iterator.hasNext())
    {
      RedBlackNode<Long, SimultaneousEvents> node = iterator.next();
      eventsMap.put(node.getKey(), node.getObject());
    }
    return eventsMap;
  }

  public long getTime()
  {
    return time;
  }
}

