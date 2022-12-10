package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.voltage.VoltageRepresentation;
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
  protected int numberOfDivsWide;
  protected int samplesPerDiv;
  protected VoltageRepresentation colours;

  public Oscilloscope(Circuit circuit,
                      String name,
                      OscilloscopePins pins,
                      float sampleFrequency,
                      int numberOfDivsWide,
                      int samplesPerDiv,
                      VoltageRepresentation colours)
  {
    super(circuit, name, pins);
    this.sampleFrequency = sampleFrequency;
    this.sampleTime = frequencyToTime(sampleFrequency);
    this.numberOfDivsWide = numberOfDivsWide;
    this.samplesPerDiv = samplesPerDiv;
    this.colours = colours;
  }

  @Override
  public void executeTick(Simulation simulation)
  {
    new TickEvent(sampleTime, this, simulation.getTimeline());
    List<Port> inputs = pins.getInputs();
    state.tick();
    long time = simulation.getTime();
    for (int input = 0; input < inputs.size(); input++)
    {
      Port port = inputs.get(input);
      TraceNet trace = port.getTrace();
      state.sample(input, trace, colours, time);
    }
  }

  @Override
  public State createState(Simulation simulation)
  {
    return new OscilloscopeState(pins.getInputs().size(), numberOfDivsWide * samplesPerDiv);
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

