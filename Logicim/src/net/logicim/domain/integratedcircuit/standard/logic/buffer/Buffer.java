package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.trace.TraceValue;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;

public class Buffer
    extends IntegratedCircuit<BufferPins, Stateless>
{
  public Buffer(Circuit circuit, String name, BufferPins pins)
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
    Port input = pins.getInput();
    TraceValue inValue = input.readValue(simulation.getTime());
    if (inValue.isHigh())
    {
      pins.getOutput().writeBool(simulation.getTimeline(), transformOutput(true));
    }
    else if (inValue.isLow())
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
    return "Buffer";
  }

  public Port getInputPort()
  {
    return getPins().getInput();
  }
}

