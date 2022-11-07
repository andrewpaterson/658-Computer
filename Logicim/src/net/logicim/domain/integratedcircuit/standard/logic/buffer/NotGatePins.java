package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.propagation.BistateOutputPropagation;
import net.logicim.domain.common.propagation.InputPropagation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class NotGatePins
    extends Pins
{
  private Uniport input;
  private Uniport output;

  public NotGatePins()
  {
    super();
    output = new Uniport(Output,
                         this,
                         "Output",
                         new BistateOutputPropagation("",
                                                      0.0f,
                                                      3.3f,
                                                      nanosecondsToTime(2.5f),
                                                      nanosecondsToTime(2.5f)));
    input = new Uniport(Input,
                        this,
                        "Input",
                        new InputPropagation("", 0.8f, 2.0f));
  }

  public Uniport getInput()
  {
    return input;
  }

  public Uniport getOutput()
  {
    return output;
  }
}

