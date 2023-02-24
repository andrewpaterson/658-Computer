package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.redblacktree.TreeItem;
import net.logicim.domain.common.event.Event;

import java.util.LinkedHashSet;

public class SimultaneousEvents
    implements TreeItem<Long>
{
  protected long time;
  protected LinkedHashSet<Event> events;

  public SimultaneousEvents(long time)
  {
    this.time = time;
    this.events = new LinkedHashSet<>();
  }

  @Override
  public Long getTreeKey()
  {
    return time;
  }

  public void add(Event event)
  {
    if (event.getTime() != time)
    {
      throw new SimulatorException("Simultaneous time [" + LongTime.toNanosecondsString(time) + "] is not equal to event time [" + LongTime.toNanosecondsString(event.getTime()) + "].");
    }
    boolean alreadyContained = !events.add(event);
    if (alreadyContained)
    {
      throw new SimulatorException("Event [%s] has already been added.", event.toShortString());
    }
  }

  public void done()
  {
    time = -1;
    events = null;
  }

  public int size()
  {
    return events.size();
  }

  public long getTime()
  {
    return time;
  }

  public Event getFirst()
  {
    if (events.size() > 0)
    {
      return events.iterator().next();
    }
    else
    {
      return null;
    }
  }

  public boolean remove(Event event)
  {
    return events.remove(event);
  }
}

