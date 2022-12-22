package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.event.TickEvent;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;

import static net.logicim.domain.common.LongTime.frequencyToTime;

public class ClockOscillator
    extends IntegratedCircuit<ClockOscillatorPins, ClockOscillatorState>
{
  public static final String TYPE = "Clock";
  protected long halfCycleTime;
  protected long initialisationTime;
  protected long fullTicks;

  public ClockOscillator(Circuit circuit, String name, ClockOscillatorPins pins, float frequency)
  {
    this(circuit, name, pins, frequency, frequencyToTime(frequency) / 2);
  }

  public ClockOscillator(Circuit circuit, String name, ClockOscillatorPins pins, float frequency, long initialisationTime)
  {
    super(circuit, name, pins);
    this.halfCycleTime = frequencyToTime(frequency) / 2;
    this.initialisationTime = initialisationTime;
    this.fullTicks = 0;
  }

  @Override
  public void inputTransition(Simulation simulation, LogicPort port)
  {
  }

  @Override
  public void executeTick(Simulation simulation)
  {
    if (isPowered(simulation.getTime()))
    {
      state.tick();
      if (state.getState())
      {
        fullTicks++;
      }

      createTickEvent(simulation, halfCycleTime);
      pins.getOutput().writeBool(simulation.getTimeline(), state.getState());
      if (pins.getOutput2() != null)
      {
        pins.getOutput2().writeBool(simulation.getTimeline(), !state.getState());
      }
    }
  }

  protected void createTickEvent(Simulation simulation, long halfCycleTime)
  {
    new TickEvent(halfCycleTime, this, simulation.getTimeline());
  }

  @Override
  public ClockOscillatorState createState(Simulation simulation)
  {
    return new ClockOscillatorState();
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
    createTickEvent(simulation, initialisationTime);
  }

  @Override
  public String getType()
  {
    return TYPE;
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

  public long getFullTicks()
  {
    return fullTicks;
  }

  @Override
  public void traceConnected(Simulation simulation, Port port)
  {
    super.traceConnected(simulation, port);

    if (port.isPowerIn())
    {
      if (isPowered(simulation.getTime()))
      {
        if (!hasTickEvent())
        {
          createTickEvent(simulation, halfCycleTime);
        }
      }
    }
  }

  private boolean hasTickEvent()
  {
    for (IntegratedCircuitEvent event : events)
    {
      if (event instanceof TickEvent)
      {
        return true;
      }
    }
    return false;
  }
}

