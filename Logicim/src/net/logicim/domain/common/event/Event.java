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

  public Event(long time, Timeline timeline)
  {
    this.time = time;
    id = nextId;
    nextId++;

    timeline.addFutureEvent(this);
  }

  public Event(long time, long id, Timeline timeline)
  {
    this.time = time;
    this.id = id;
    if (id >= nextId)
    {
      nextId = id + 1;
    }

    timeline.addEvent(this);
  }

  public static void resetNextId()
  {
    nextId = 1L;
  }

  public long getTime()
  {
    return time;
  }

  public abstract void execute(Simulation simulation);

  public abstract IntegratedCircuit<?, ?> getIntegratedCircuit();

  public abstract void removeFromOwner();

  public abstract EventData<?> save();

  public String toShortString()
  {
    String string = getClass().getSimpleName().replace("Event", "");
    return string + ": Time [" + time + "]";
  }
}
