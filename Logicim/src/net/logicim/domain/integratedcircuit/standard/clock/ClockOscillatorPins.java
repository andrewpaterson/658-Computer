package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.propagation.BistateOutputPropagation;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Output;

public class ClockOscillatorPins
    extends Pins
{
  protected Uniport output;

  public ClockOscillatorPins(Timeline timeline)
  {
    super(timeline);
    output = new Uniport(Output,
                         this,
                         "Out",
                         new BistateOutputPropagation(timeline,
                                                      "",
                                                      0.0f,
                                                      3.3f,
                                                      nanosecondsToTime(5.0f),
                                                      nanosecondsToTime(5.0f)));
  }

  public Uniport getOutput()
  {
    return output;
  }
}

