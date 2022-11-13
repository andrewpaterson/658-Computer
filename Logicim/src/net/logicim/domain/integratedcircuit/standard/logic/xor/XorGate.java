package net.logicim.domain.integratedcircuit.standard.logic.xor;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.trace.TraceValue;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;

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
  public ClockOscillatorState simulationStarted(Simulation simulation)
  {
    return null;
  }

  @Override
  public void inputTransition(Simulation simulation, Port port)
  {
    List<Uniport> inputs = pins.getInputs();
    boolean unsettled = false;
    for (Uniport input : inputs)
    {
      TraceValue inValue = input.readValue();
      if (inValue.isImpedance() ||
          inValue.isUnsettled())
      {
        unsettled = true;
        break;
      }
    }
    if (unsettled)
    {
      pins.getOutput().writeUnsettled(simulation.getTimeline());
      return;
    }

    int highs = 0;
    for (Uniport input : inputs)
    {
      TraceValue inValue = input.readValue();
      if (inValue.isHigh())
      {
        highs++;
      }
    }
    boolean value = highs % 2 == 1;
    pins.getOutput().writeBool(simulation.getTimeline(), transformOutput(value));
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

