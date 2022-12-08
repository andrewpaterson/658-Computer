package net.logicim.domain.integratedcircuit.extra;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Input;

public class OscilloscopePins
    extends Pins
{
  private List<Port> inputs;

  public OscilloscopePins(int inputCount)
  {
    inputs = new ArrayList<>(inputCount);
    for (int i = 0; i < inputCount; i++)
    {
      Port port = new Port(Input,
                           this,
                           "Input " + i,
                           null);
      inputs.add(port);
    }
  }

  public List<Port> getInputs()
  {
    return inputs;
  }
}

