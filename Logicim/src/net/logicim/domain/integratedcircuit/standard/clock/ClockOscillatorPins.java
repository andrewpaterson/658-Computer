package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.BistateOutputVoltage;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Output;

public class ClockOscillatorPins
    extends Pins
{
  protected Port output;

  public ClockOscillatorPins()
  {
    super();
    output = new Port(Output,
                      this,
                      "Output",
                      new BistateOutputVoltage("",
                                                  0.0f,
                                                  3.3f,
                                                  nanosecondsToTime(2.0f),
                                                  nanosecondsToTime(2.0f)));
  }

  public Port getOutput()
  {
    return output;
  }
}

