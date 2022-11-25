package net.logicim.domain.integratedcircuit.standard.logic.xor;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.trace.TraceValue;

import java.util.List;

public class XorGate
    extends IntegratedCircuit<XorGatePins, Stateless>
{
  public XorGate(Circuit circuit, String name, XorGatePins pins)
  {
    super(circuit, name, pins);
    setState(new Stateless(this));
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public State createState(Simulation simulation)
  {
    return null;
  }

  @Override
  public void inputTransition(Simulation simulation, Port port)
  {
    List<Port> inputs = pins.getInputs();

    int highs = 0;
    int lows = 0;
    for (Port input : inputs)
    {
      TraceValue inValue = input.readValue(simulation.getTime());
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
      pins.getOutput().writeBool(simulation.getTimeline(), transformOutput(value));
    }

  }

  protected boolean transformOutput(boolean value)
  {
    return value;
  }

  @Override
  public String getType()
  {
    return "XOR Gate";
  }
}

