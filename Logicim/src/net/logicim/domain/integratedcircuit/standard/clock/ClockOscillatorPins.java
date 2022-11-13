package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.propagation.BistateOutputVoltage;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Output;

public class ClockOscillatorPins
    extends Pins
{
  protected Uniport output;

  public ClockOscillatorPins()
  {
    super();
    output = new Uniport(Output,
                         this,
                         "Output",
                         new BistateOutputVoltage("",
                                                  0.0f,
                                                  3.3f,
                                                  nanosecondsToTime(2.0f),
                                                  nanosecondsToTime(2.0f)));
  }

  public Uniport getOutput()
  {
    return output;
  }
}

