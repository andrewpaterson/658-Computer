package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.redblacktree.RedBlackNode;
import net.logicim.common.collection.redblacktree.RedBlackTree;
import net.logicim.data.circuit.TimelineData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.event.Event;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.logicim.domain.common.LongTime.toNanosecondsString;

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

  public void addFutureEvent(Event event)
  {
    boolean b = addEvent(event);
    if (!b)
    {
      throw new SimulatorException("Cannot add event in the past.  Event time [" + toNanosecondsString(eventTime) + "] must be after current time [" + toNanosecondsString(time) + "].");
    }
  }

  public boolean addEvent(Event event)
  {
    long eventTime = event.getTime();
    if (eventTime < time || eventTime == time)
    {
      return false;
    }
    SimultaneousEvents simultaneousEvents = events.find(eventTime);
    if (simultaneousEvents == null)
    {
      simultaneousEvents = new SimultaneousEvents(eventTime);
      events.add(simultaneousEvents);
    }
    simultaneousEvents.add(event);
    return true;
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
          throw new SimulatorException("Cannot update simulation time.  Event time [" + toNanosecondsString(events.time) + "] must be after current time [" + toNanosecondsString(time) + "].");
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
        throw new SimulatorException("Cannot update simulation time.  Event time [" + toNanosecondsString(events.time) + "] must be after current time [" + toNanosecondsString(time) + "].");
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

  public boolean remove(Event event)
  {
    SimultaneousEvents simultaneousEvents = events.find(event.getTime());
    event.removeFromOwner();
    if (simultaneousEvents != null)
    {
      return simultaneousEvents.getEvents().remove(event);
    }
    else
    {
      return false;
    }
  }

  public boolean removeAll(List<? extends Event> events)
  {
    boolean result = false;
    for (Event event : events)
    {
      result |= remove(event);
    }
    return result;
  }

  public TimelineData save()
  {
    return new TimelineData(time, previousEventTime, eventTime);
  }

  public void load(TimelineData timelineData)
  {
    this.time = timelineData.time;
    this.previousEventTime = timelineData.previousEventTime;
    this.eventTime = timelineData.eventTime;
  }
}

