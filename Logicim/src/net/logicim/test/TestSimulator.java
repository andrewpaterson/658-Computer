package net.logicim.test;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.integratedcircuit.nexperia.LVC541;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;

public class TestSimulator
{
  public static void main(String[] args)
  {
    Timeline timeline = new Timeline();
    new ClockOscillator("Henry", new ClockOscillatorPins(timeline));

    timeline.run();
  }
}

