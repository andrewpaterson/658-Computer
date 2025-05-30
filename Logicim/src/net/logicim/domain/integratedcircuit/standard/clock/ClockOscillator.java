package net.logicim.domain.integratedcircuit.standard.clock;

import net.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.event.IntegratedCircuitEvent;
import net.logicim.domain.common.event.TickEvent;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import static net.logicim.domain.common.LongTime.frequencyToTime;

public class ClockOscillator
    extends IntegratedCircuit<ClockOscillatorPins, ClockOscillatorState>
{
  public static final String TYPE = "Clock";

  protected long halfCycleTime;
  protected long initialisationTime;
  protected long fullTicks;

  public ClockOscillator(SubcircuitSimulation containingSubcircuitSimulation,
                         String name,
                         ClockOscillatorPins pins,
                         float frequency)
  {
    this(containingSubcircuitSimulation,
         name,
         pins,
         frequency,
         frequencyToTime(frequency) / 2);
  }

  public ClockOscillator(SubcircuitSimulation containingSubcircuitSimulation, String name, ClockOscillatorPins pins, float frequency, long initialisationTime)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
    this.halfCycleTime = frequencyToTime(frequency) / 2;
    this.initialisationTime = initialisationTime;
    this.fullTicks = 0;
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
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
      if (pins.getOutputInverse() != null)
      {
        pins.getOutputInverse().writeBool(simulation.getTimeline(), !state.getState());
      }
    }
  }

  protected void createTickEvent(Simulation simulation, long halfCycleTime)
  {
    new TickEvent(halfCycleTime, this, simulation.getTimeline());
  }

  @Override
  public ClockOscillatorState createState()
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

  public float getInternalVoltage(long time)
  {
    if (state != null)
    {
      return getPins().getOutput().getVoltageConfigurationSource().getVoltageOut(state.getState(), getVCC(time));
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
      else
      {
        LogicPort output = pins.getOutput();
        output.writeImpedance(simulation.getTimeline());
        if (pins.getOutputInverse() != null)
        {
          pins.getOutputInverse().writeImpedance(simulation.getTimeline());
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

