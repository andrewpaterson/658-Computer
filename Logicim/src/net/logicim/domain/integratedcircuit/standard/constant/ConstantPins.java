package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import static net.logicim.domain.common.port.PortType.Output;

public class ConstantPins
    extends Pins
{
  protected LogicPort output;

  public ConstantPins(VoltageConfigurationSource voltageConfiguration)
  {
    super();
    output = new LogicPort(Output,
                           this,
                           "Output",
                           voltageConfiguration);
  }

  public LogicPort getOutput()
  {
    return output;
  }
}

