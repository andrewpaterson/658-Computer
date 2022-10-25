package net.logicim.domain.integratedcircuit.standard.logic;

import net.logicim.domain.common.*;
import net.logicim.domain.common.propagation.BistateOutputPropagation;
import net.logicim.domain.common.propagation.InputPropagation;

public class NotGatePins
    extends Pins
{
  private Uniport input;
  private Uniport output;

  public NotGatePins(Timeline timeline)
  {
    super(timeline);
    output = new Uniport(this, "Output", new BistateOutputPropagation(timeline,
                                                                      "",
                                                                      0.0f,
                                                                      3.3f,
                                                                      LongTime.nanosecondsToTime(2.5f),
                                                                      LongTime.nanosecondsToTime(2.5f)));
    input = new Uniport(this, "Input", new InputPropagation("", 0.8f, 2.0f));
    addPort(output);
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

