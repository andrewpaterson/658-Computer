package net.logicim.domain.integratedcircuit.standard.logic.xor;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public class XorGate
    extends IntegratedCircuit<XorGatePins, Stateless>
{
  public static final String TYPE = "XOR Gate";

  public XorGate(SubcircuitSimulation containingSubcircuitSimulation,
                 String name,
                 XorGatePins pins)
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
      TraceValue inValue = input.readValue(timeline);
      if (inValue.isHigh())
      {
        highs++;
      }
      if (inValue.isLow())
      {
        lows++;
      }
    }
    if ((highs > 0 || lows > 0))
    {
      boolean value = highs % 2 == 1;
      pins.getOutput().writeBool(timeline, transformOutput(value));
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
  public Stateless createState()
  {
    return new Stateless();
  }
}

