package net.logicim.domain.integratedcircuit.standard.logic.xor;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.VoltageConfiguration;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class XorGatePins
    extends Pins
{
  private List<Port> inputs;
  private Port output;

  public XorGatePins(int inputCount, VoltageConfiguration voltageConfiguration)
  {
    super();
    output = new Port(Output,
                      this,
                      "Output",
                      voltageConfiguration);

    inputs = new ArrayList<>(inputCount);
    for (int i = 0; i < inputCount; i++)
    {
      Port port = new Port(Input,
                           this,
                           "Input " + i,
                           voltageConfiguration);
      inputs.add(port);
    }
  }

  public Port getOutput()
  {
    return output;
  }

  public List<Port> getInputs()
  {
    return inputs;
  }
}

