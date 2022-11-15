package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.BistateOutputVoltage;
import net.logicim.domain.common.propagation.InputVoltage;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class BufferPins
    extends Pins
{
  private Port input;
  private Port output;

  public BufferPins()
  {
    super();
    output = new Port(Output,
                      this,
                      "Output",
                      new BistateOutputVoltage("",
                                               0.0f,
                                               3.3f,
                                               nanosecondsToTime(2.5f),
                                               nanosecondsToTime(2.5f)));
    input = new Port(Input,
                     this,
                     "Input",
                     new InputVoltage("", 0.8f, 2.0f));
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

