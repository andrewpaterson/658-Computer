package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.propagation.BistateOutputPropagation;
import net.logicim.domain.common.propagation.InputPropagation;
import net.logicim.domain.common.trace.TraceNet;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.InternalInput;
import static net.logicim.domain.common.port.PortType.Output;

public class ClockOscillatorPins
    extends Pins
{
  protected Uniport internalIn;
  protected Uniport internalOut;
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
                                                      nanosecondsToTime(2.5f),
                                                      nanosecondsToTime(2.5f)));
    internalIn = new Uniport(InternalInput,
                             this,
                             "Tick",
                             new InputPropagation("",
                                                  0.0f,
                                                  3.3f));
    internalOut = new Uniport(Output,
                              this,
                              "Tock",
                              new BistateOutputPropagation(timeline,
                                                           "",
                                                           0.0f,
                                                           3.3f,
                                                           nanosecondsToTime(2.5f),
                                                           nanosecondsToTime(2.5f)));
    TraceNet trace = new TraceNet();
    internalIn.connect(trace);
    internalOut.connect(trace);
  }

  public Uniport getInternalIn()
  {
    return internalIn;
  }

  public Uniport getInternalOut()
  {
    return internalOut;
  }

  public Uniport getOutput()
  {
    return output;
  }
}

