package net.logicim.domain.integratedcircuit.standard.logic.or;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
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
    List<Port> inputs = pins.getInputs();
    boolean unsettled = false;
    boolean value = false;
    for (Port input : inputs)
    {
      TraceValue inValue = input.readValue(simulation.getTime());
      if (inValue.isHigh())
      {
        value = true;
        break;
      }
      if (inValue.isImpedance() ||
          inValue.isUnsettled())
      {
        unsettled = true;
      }
    }
    if (value || !unsettled)
    {
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
    return "OR Gate";
  }
}

