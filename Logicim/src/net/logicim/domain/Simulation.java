package net.logicim.domain;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.event.Event;

public class Simulation
{
  protected Timeline timeline;

  public Simulation()
  {
    this.timeline = new Timeline(this);
  }

  public Timeline getTimeline()
  {
    return timeline;
  }

  public boolean runSimultaneous()
  {
    return timeline.runSimultaneous();
  }

  public boolean runToTime(long timeForward)
  {
    return timeline.runToTime(timeForward);
  }

  public long getTime()
  {
    return timeline.getTime();
  }

  public void removeEvent(Event event)
  {
    timeline.remove(event);
  }
}

