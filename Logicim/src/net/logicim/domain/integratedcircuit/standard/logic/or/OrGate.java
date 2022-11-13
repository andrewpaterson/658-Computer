package net.logicim.domain.integratedcircuit.standard.logic.or;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.trace.TraceValue;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;

import java.util.List;

public class OrGate
    extends IntegratedCircuit<OrGatePins, Stateless>
{
  public OrGate(Circuit circuit, String name, OrGatePins pins)
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

    boolean value = false;
    for (Uniport input : inputs)
    {
      TraceValue inValue = input.readValue();
      if (inValue.isHigh())
      {
        value = true;
        break;
      }
    }
    pins.getOutput().writeBool(simulation.getTimeline(), transformOutput(value));
  }

  protected boolean transformOutput(boolean value)
  {
    return value;
  }

  @Override
  public String getType()
  {
    return "OR Gate";
  }
}

