package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import static net.logicim.domain.common.port.PortType.Output;

public class ClockOscillatorPins
    extends Pins
{
  protected LogicPort output;
  protected LogicPort output2;

  public ClockOscillatorPins(VoltageConfigurationSource voltageConfiguration, boolean inverseOut)
  {
    super();
    output = new LogicPort(Output,
                           this,
                           "Output",
                           voltageConfiguration);
    if (inverseOut)
    {
      output2 = new LogicPort(Output,
                              this,
                              "Output2",
                              voltageConfiguration);
    }
  }

  public LogicPort getOutput()
  {
    return output;
  }

  public LogicPort getOutput2()
  {
    return output2;
  }
}

