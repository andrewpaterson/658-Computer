package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import static net.logicim.domain.common.port.PortType.Output;

public class ConstantPins
    extends Pins
{
  protected Port output;

  public ConstantPins(VoltageConfigurationSource voltageConfiguration)
  {
    super();
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

