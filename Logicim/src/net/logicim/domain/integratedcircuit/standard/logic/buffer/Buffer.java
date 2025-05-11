package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.Stateless;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import java.util.List;

public class Buffer
    extends IntegratedCircuit<BufferPins, Stateless>
{
  public static final String TYPE = "Buffer";

  public Buffer(SubcircuitSimulation containingSubcircuitSimulation,
                String name,
                BufferPins pins)
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
    if (isPowered(timeline.getTime()))
    {
      List<LogicPort> inputs = pins.getInputs();
      List<LogicPort> outputs = pins.getOutputs();

      for (int i = 0; i < inputs.size(); i++)
      {
        LogicPort input = inputs.get(i);
        LogicPort output = outputs.get(i);
        TraceValue inValue = input.readValue(timeline.getTime());
        if (inValue.isHigh())
        {
          output.writeBool(timeline, transformOutput(true));
        }
        else if (inValue.isLow())
        {
          output.writeBool(timeline, transformOutput(false));
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

