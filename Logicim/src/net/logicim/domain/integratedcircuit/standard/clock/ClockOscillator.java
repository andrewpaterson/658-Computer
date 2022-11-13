package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.LongTime;
import net.logicim.domain.common.port.Port;

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
  public void inputTransition(Simulation simulation, Port port)
  {
  }

  @Override
  public void executeTick(Simulation simulation)
  {
    state.tick();

    simulation.getTimeline().createTickEvent(halfCycleTime, this);
    pins.getOutput().writeBool(simulation.getTimeline(), state.getState());
  }

  @Override
  public ClockOscillatorState simulationStarted(Simulation simulation)
  {
    simulation.getTimeline().createTickEvent(halfCycleTime, this);

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

  public float getInternalVoltage()
  {
    if (state != null)
    {
      if (state.getState())
      {
        return 3.3f;
      }
      else
      {
        return 0.0f;
      }
    }
    else
    {
      throw new SimulatorException("Clock without state cannot get internal voltage.");
    }
  }
}

