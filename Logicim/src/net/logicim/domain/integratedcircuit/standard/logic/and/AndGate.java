package net.logicim.domain.integratedcircuit.standard.logic.and;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816State;

import java.util.List;

public class AndGate
    extends IntegratedCircuit<AndGatePins, Stateless>
{
  public static final String TYPE = "AND Gate";

  public AndGate(Circuit circuit, String name, AndGatePins pins)
  {
    super(circuit, name, pins);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public void inputTransition(Simulation simulation, LogicPort port)
  {
    List<LogicPort> inputs = pins.getInputs();

    int lows = 0;
    int highs = 0;
    for (LogicPort input : inputs)
    {
      TraceValue inValue = input.readValue(simulation.getTime());
      if (inValue.isLow())
      {
        lows++;
      }
      else if (inValue.isHigh())
      {
        highs++;
      }
    }

    if (lows > 0)
    {
      pins.getOutput().writeBool(simulation.getTimeline(), transformOutput(false));
    }
    else if (highs > 0)
    {
      pins.getOutput().writeBool(simulation.getTimeline(), transformOutput(true));
    }
  }

  protected boolean transformOutput(boolean value)
  {
    return value;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  @Override
  public State createState()
  {
    return new W65C816State();
  }
}

