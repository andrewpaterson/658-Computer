package net.logicim.domain.integratedcircuit.standard.logic.or;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.wire.TraceValue;

import java.util.List;

public class OrGate
    extends IntegratedCircuit<OrGatePins, Stateless>
{
  public static final String TYPE = "OR Gate";

  public OrGate(Circuit circuit, String name, OrGatePins pins)
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
    int highs = 0;
    int lows = 0;
    for (LogicPort input : inputs)
    {
      TraceValue inValue = input.readValue(simulation.getTime());
      if (inValue.isHigh())
      {
        highs++;
      }
      else if (inValue.isLow())
      {
        lows++;
      }
    }
    if (highs > 0)
    {
      pins.getOutput().writeBool(simulation.getTimeline(), transformOutput(true));
    }
    else if (lows > 0)
    {
      pins.getOutput().writeBool(simulation.getTimeline(), transformOutput(false));
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
}

