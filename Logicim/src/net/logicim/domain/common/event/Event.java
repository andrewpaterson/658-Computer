package net.logicim.domain.common.event;

import net.logicim.data.common.event.EventData;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;

public abstract class Event
{
  public static long nextId = 1L;

  protected long time;
  protected long id;
  protected boolean removed;
  protected boolean executed;

  public Event(long time, Timeline timeline)
  {
    this.time = time;
    this.id = nextId++;
    removed = false;
    executed = false;
    timeline.addFutureEvent(this);
  }

  public Event(long time, Timeline timeline, long id)
  {
    this.time = time;
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }
    removed = false;
    timeline.addEvent(this);
  }

  public void removeFromOwner()
  {
    removed = true;
  }

  public boolean isRemoved()
  {
    return removed;
  }

  public boolean isExecuted()
  {
    return executed;
  }

  public long getTime()
  {
    return time;
  }

  public void execute(Simulation simulation)
  {
    executed = true;
    removeFromOwner();
  }


  public abstract IntegratedCircuit<?, ?> getIntegratedCircuit();

  public abstract EventData<?> save();

  public String toShortString()
  {
    String string = getClass().getSimpleName().replace("Event", "");
    return string + ": Time [" + time + "]";
  }

  public static void resetNextId()
  {
    nextId = 1;
  }
}

