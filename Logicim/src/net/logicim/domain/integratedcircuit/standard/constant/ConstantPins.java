package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Omniport;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.propagation.BistateOutputVoltage;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Output;

public class ConstantPins
    extends Pins
{
  protected byte width;
  protected Port output;

  public ConstantPins(byte width)
  {
    super();
    this.width = width;
    BistateOutputVoltage voltageConfiguration = new BistateOutputVoltage("",
                                                                         0.0f,
                                                                         3.3f,
                                                                         nanosecondsToTime(2.0f),
                                                                         nanosecondsToTime(2.0f));
    if (width == 1)
    {
      output = new Uniport(Output,
                           this,
                           "Output",
                           voltageConfiguration);
    }
    else if (width > 1)
    {
      output = new Omniport(Output,
                            this,
                            "Output",
                            width,
                            voltageConfiguration);
    }
    else
    {
      throw new SimulatorException("Cannot have constant width of zero.");
    }
  }

  public Port getOutput()
  {
    return output;
  }
}

