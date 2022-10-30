package net.logicim.domain.integratedcircuit.standard.logic;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.trace.TraceValue;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;

import java.util.List;

public class NotGate
    extends IntegratedCircuit<NotGatePins, Stateless>
{
  public NotGate(Circuit circuit, String name, NotGatePins pins)
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
  public void inputTraceChanged(Simulation simulation, List<Port> updatedPorts)
  {
    Uniport input = pins.getInput();
    TraceValue inValue = input.readValue();
    if (inValue.isError() || inValue.isImpedance())
    {
      pins.getOutput().writeUnsettled();
    }
    else
    {
      if (inValue.isHigh())
      {
        pins.getOutput().writeBool(simulation.getTimeline(), false);
      }
      else if (inValue.isLow())
      {
        pins.getOutput().writeBool(simulation.getTimeline(), true);
      }
    }
  }

  @Override
  public String getType()
  {
    return "NOT Gate";
  }
}

