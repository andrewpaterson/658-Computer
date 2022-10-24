package net.logicim.domain.common;

import net.logicim.common.collection.redblacktree.RedBlackTree;

import java.util.ArrayList;
import java.util.List;

public class Timeline
{
  protected List<Pins> tickables;
  protected long time;
  protected RedBlackTree<Long, SimultaneousEvents> events;

  public Timeline()
  {
    this.tickables = new ArrayList<>();
    time = 0;
  }

  public void add(Pins tickable)
  {
    tickables.add(tickable);
  }

  public Event createPropagationEvent(Trace trace, float outVoltage, int propagationDelay)
  {
    long eventTime = this.time + propagationDelay;
    Event event = new Event(eventTime, outVoltage, trace);
    SimultaneousEvents simultaneousEvents = events.find(eventTime);
    if (simultaneousEvents == null)
    {
      simultaneousEvents = new SimultaneousEvents(time);
      events.add(simultaneousEvents);
    }
    simultaneousEvents.add(event);
    trace.add(event);
    return event;
  }
}

