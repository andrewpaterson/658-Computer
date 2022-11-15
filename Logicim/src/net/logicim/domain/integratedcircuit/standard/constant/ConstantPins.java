package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.BistateOutputVoltage;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Output;

public class ConstantPins
    extends Pins
{
  protected Port output;

  public ConstantPins()
  {
    super();
    BistateOutputVoltage voltageConfiguration = new BistateOutputVoltage("",
                                                                         0.0f,
                                                                         3.3f,
                                                                         nanosecondsToTime(2.0f),
                                                                         nanosecondsToTime(2.0f));
    output = new Port(Output,
                      this,
                      "Output",
                      voltageConfiguration);
  }

  public Port getOutput()
  {
    return output;
  }
}

