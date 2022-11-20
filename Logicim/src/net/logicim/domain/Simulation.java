package net.logicim.domain;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.event.PortEvent;
import net.logicim.domain.common.state.State;

import java.util.LinkedHashMap;
import java.util.Map;

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


  public void run()
  {
    timeline.run();
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

  public void removeEvent(PortEvent event)
  {
    timeline.remove(event);
  }
}

