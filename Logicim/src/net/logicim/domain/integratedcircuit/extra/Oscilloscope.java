package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.event.TickEvent;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.voltage.VoltageRepresentation;
import net.logicim.domain.common.wire.Trace;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

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

  public Oscilloscope(SubcircuitSimulation containingSubcircuitSimulation,
                      String name,
                      OscilloscopePins pins,
                      float sampleFrequency,
                      int numberOfDivsWide,
                      int samplesPerDiv,
                      VoltageRepresentation colours)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
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
    List<LogicPort> inputs = pins.getInputs();
    state.tick();
    long time = simulation.getTime();
    for (int input = 0; input < inputs.size(); input++)
    {
      LogicPort port = inputs.get(input);
      Trace trace = port.getTrace();
      state.sample(input, trace, colours, time);
    }
  }

  @Override
  public OscilloscopeState createState()
  {
    return new OscilloscopeState(pins.getInputs().size(), numberOfDivsWide * samplesPerDiv);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
    new TickEvent(sampleTime, this, simulation.getTimeline());
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

