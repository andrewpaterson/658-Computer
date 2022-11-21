package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.VoltageConfiguration;

import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class BufferPins
    extends Pins
{
  private Port input;
  private Port output;

  public BufferPins(VoltageConfiguration voltageConfiguration)
  {
    super();
    output = new Port(Output,
                      this,
                      "Output",
                      voltageConfiguration);
    input = new Port(Input,
                     this,
                     "Input",
                     voltageConfiguration);
  }

  public Port getInput()
  {
    return input;
  }

  public Port getOutput()
  {
    return output;
  }
}

