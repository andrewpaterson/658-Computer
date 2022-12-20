package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.trace.TraceValue;

public class Buffer
    extends IntegratedCircuit<BufferPins, Stateless>
{
  public static final String TYPE = "Buffer";

  public Buffer(Circuit circuit, String name, BufferPins pins)
  {
    super(circuit, name, pins);
    setState(new Stateless());
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
  public void inputTransition(Simulation simulation, LogicPort port)
  {
    LogicPort input = pins.getInput();
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
    return TYPE;
  }

  public LogicPort getInputPort()
  {
    return getPins().getInput();
  }
}

