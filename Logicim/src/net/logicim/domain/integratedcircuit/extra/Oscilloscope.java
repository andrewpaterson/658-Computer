package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.event.TickEvent;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.trace.TraceNet;

import java.util.List;

import static net.logicim.domain.common.LongTime.frequencyToTime;

public class Oscilloscope
    extends IntegratedCircuit<OscilloscopePins, OscilloscopeState>
{
  public static final String TYPE = "Oscilloscope";

  protected float sampleFrequency;
  protected long sampleTime;
  protected int width;
  protected int resolution;

  public Oscilloscope(Circuit circuit, String name, OscilloscopePins pins, float sampleFrequency, int width, int resolution)
  {
    super(circuit, name, pins);
    this.sampleFrequency = sampleFrequency;
    this.sampleTime = frequencyToTime(sampleFrequency);
    this.width = width;
    this.resolution = resolution;
  }

  @Override
  public void executeTick(Simulation simulation)
  {
    new TickEvent(sampleTime, this, simulation.getTimeline());
    List<Port> inputs = pins.getInputs();
    state.tick();
    for (int i = 0; i < inputs.size(); i++)
    {
      Port port = inputs.get(i);
      TraceNet trace = port.getTrace();
      float voltage;
      if (trace != null)
      {
        voltage = trace.getVoltage(simulation.getTime());
      }
      else
      {
        voltage = Float.NaN;
      }
      state.sample(i, voltage);
    }
  }

  @Override
  public State createState(Simulation simulation)
  {
    return new OscilloscopeState(this, width, pins.getInputs().size(), resolution);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
    new TickEvent(sampleTime, this, simulation.getTimeline());
  }

  @Override
  public void inputTransition(Simulation simulation, Port port)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

