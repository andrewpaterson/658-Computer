package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.wire.TraceValue;

import java.util.List;

public class Buffer
    extends IntegratedCircuit<BufferPins, Stateless>
{
  public static final String TYPE = "Buffer";

  public Buffer(Circuit circuit, String name, BufferPins pins)
  {
    super(circuit, name, pins);
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  @Override
  public void inputTransition(Simulation simulation, LogicPort port)
  {
    if (isPowered(simulation.getTime()))
    {
      List<LogicPort> inputs = pins.getInputs();
      List<LogicPort> outputs = pins.getOutputs();

      for (int i = 0; i < inputs.size(); i++)
      {
        LogicPort input = inputs.get(i);
        LogicPort output = outputs.get(i);
        TraceValue inValue = input.readValue(simulation.getTime());
        if (inValue.isHigh())
        {
          output.writeBool(simulation.getTimeline(), transformOutput(true));
        }
        else if (inValue.isLow())
        {
          output.writeBool(simulation.getTimeline(), transformOutput(false));
        }
      }
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

