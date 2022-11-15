package net.logicim.domain;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.state.State;

import java.util.LinkedHashMap;
import java.util.Map;

public class Simulation
{
  protected Timeline timeline;
  protected Map<IntegratedCircuit<?, ?>, State> states;

  public Simulation()
  {
    this.timeline = new Timeline(this);
    this.states = new LinkedHashMap<>();
  }

  public Timeline getTimeline()
  {
    return timeline;
  }

  public void addState(State state)
  {
    states.put(state.getParent(), state);
  }

  public void run()
  {
    timeline.run();
  }

  public boolean runSimultaneous()
  {
    return timeline.runSimultaneous();
  }

  public State getState(IntegratedCircuit<? extends Pins, ? extends State> integratedCircuit)
  {
    return states.get(integratedCircuit);
  }

  public void runToTime(long timeForward)
  {
    timeline.runToTime(timeForward);
  }

  public long getTime()
  {
    return timeline.getTime();
  }
}

