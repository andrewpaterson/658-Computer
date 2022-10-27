package net.logicim.test;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.integratedcircuit.nexperia.LVC541;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.logic.NotGate;
import net.logicim.domain.integratedcircuit.standard.logic.NotGatePins;

import static net.logicim.domain.common.Units.MHz;

public class TestSimulator
{
  public static void main(String[] args)
  {
    Timeline timeline = new Timeline();
    ClockOscillator clock = new ClockOscillator("10Mhz", new ClockOscillatorPins(timeline), 10 * MHz);
    TraceNet clockTrace = new TraceNet();
    clock.getPins().getOutput().connect(clockTrace);

    NotGate notGate = new NotGate("Not", new NotGatePins(timeline));
    notGate.getPins().getInput().connect(clockTrace);
    notGate.getPins().getOutput().connect(new TraceNet());

    timeline.run();
  }
}

