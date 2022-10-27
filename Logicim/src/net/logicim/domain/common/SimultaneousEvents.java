package net.logicim.domain.common;

import net.logicim.common.SimulatorException;
import net.logicim.common.collection.redblacktree.TreeItem;

import java.util.ArrayList;
import java.util.List;

public class SimultaneousEvents
    implements TreeItem<Long>
{
  protected long time;
  protected List<Event> events;

  public SimultaneousEvents(long time)
  {
    this.time = time;
    this.events = new ArrayList<>();
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
      throw new SimulatorException("Simultaneous time [" + LongTime.timeToNanoseconds(time) + "] is not equal to event time [" + LongTime.timeToNanoseconds(event.getTime()) + "].");
    }
    events.add(event);
  }

  public void done()
  {
    time = -1;
    events = null;
  }
}

