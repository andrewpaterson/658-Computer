package net.logicim.domain.integratedcircuit.standard.logic.or;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public class OrGate
    extends IntegratedCircuit<OrGatePins, Stateless>
{
  public static final String TYPE = "OR Gate";

  public OrGate(SubcircuitSimulation containingSubcircuitSimulation,
                String name,
                OrGatePins pins)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
    List<LogicPort> inputs = pins.getInputs();
    int highs = 0;
    int lows = 0;
    for (LogicPort input : inputs)
    {
      TraceValue inValue = input.readValue(timeline.getTime());
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
      pins.getOutput().writeBool(timeline, transformOutput(true));
    }
    else if (lows > 0)
    {
      pins.getOutput().writeBool(timeline, transformOutput(false));
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

