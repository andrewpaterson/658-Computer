package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.LongTime;
import net.logicim.domain.common.port.Port;

import java.util.List;

public class ClockOscillator
    extends IntegratedCircuit<ClockOscillatorPins, ClockOscillatorState>
{
  protected long halfCycleTime;

  public ClockOscillator(Circuit circuit, String name, ClockOscillatorPins pins, float frequency)
  {
    super(circuit, name, pins);
    halfCycleTime = LongTime.secondsToTime((1.0f / frequency) / 2.0f);
  }

  @Override
  public void inputTraceChanged(Simulation simulation, List<Port> updatedPorts)
  {
  }

  @Override
  public void clockChanged(Simulation simulation)
  {
    state.tick();

    simulation.getTimeline().createClockEvent(halfCycleTime, this);
    pins.getOutput().writeBool(simulation.getTimeline(), state.getState());
  }


  @Override
  public ClockOscillatorState simulationStarted(Simulation simulation)
  {
    simulation.getTimeline().createClockEvent(halfCycleTime, this);

    return new ClockOscillatorState(this);
  }

  public long getHalfCycleTime()
  {
    return halfCycleTime;
  }

  @Override
  public String getType()
  {
    return "Clock";
  }
}

