package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.LongTime;
import net.logicim.domain.common.port.Port;

import java.util.List;

public class ClockOscillator
    extends IntegratedCircuit<ClockOscillatorPins>
{
  protected boolean state;
  protected long halfCycleTime;

  public ClockOscillator(String name, ClockOscillatorPins pins, float frequency)
  {
    super(name, pins);
    state = false;
    halfCycleTime = LongTime.secondsToTime((1.0f / frequency) / 2.0f);

    getTimeline().createClockEvent(halfCycleTime, this);
  }

  @Override
  public void inputTraceChanged(long time, List<Port> updatedPorts)
  {
  }

  @Override
  public void clockChanged(long time)
  {
    state = !state;

    getTimeline().createClockEvent(halfCycleTime, this);
    pins.getOutput().writeBool(state);
  }

  @Override
  public String getType()
  {
    return "Clock";
  }
}

