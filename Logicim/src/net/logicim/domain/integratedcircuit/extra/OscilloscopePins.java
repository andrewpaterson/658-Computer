package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Input;

public class OscilloscopePins
    extends Pins
{
  private List<LogicPort> inputs;

  public OscilloscopePins(int inputCount)
  {
    inputs = new ArrayList<>(inputCount);
    for (int i = 0; i < inputCount; i++)
    {
      LogicPort port = new LogicPort(Input,
                                     this,
                                     "Input " + i,
                                     null);
      inputs.add(port);
    }
  }

  public List<LogicPort> getInputs()
  {
    return inputs;
  }
}

