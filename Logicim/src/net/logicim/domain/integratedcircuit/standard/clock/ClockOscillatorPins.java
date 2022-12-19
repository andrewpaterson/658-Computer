package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import static net.logicim.domain.common.port.PortType.Output;

public class ClockOscillatorPins
    extends Pins
{
  protected Port output;
  protected Port output2;

  public ClockOscillatorPins(VoltageConfigurationSource voltageConfiguration, boolean inverseOut)
  {
    super();
    output = new Port(Output,
                      this,
                      "Output",
                      voltageConfiguration);
    if (inverseOut)
    {
      output2 = new Port(Output,
                        this,
                        "Output2",
                        voltageConfiguration);
    }
  }

  public Port getOutput()
  {
    return output;
  }

  public Port getOutput2()
  {
    return output2;
  }
}

