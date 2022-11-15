package net.logicim.domain.integratedcircuit.standard.logic.and;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.trace.TraceValue;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;

import java.util.List;

public class AndGate
    extends IntegratedCircuit<AndGatePins, Stateless>
{
  public AndGate(Circuit circuit, String name, AndGatePins pins)
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
    for (Port input : inputs)
    {
      TraceValue inValue = input.readValue(simulation.getTime());
      if (inValue.isImpedance())
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

    boolean value = true;
    for (Port input : inputs)
    {
      TraceValue inValue = input.readValue(simulation.getTime());
      if (inValue.isLow())
      {
        value = false;
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
    return "AND Gate";
  }
}

