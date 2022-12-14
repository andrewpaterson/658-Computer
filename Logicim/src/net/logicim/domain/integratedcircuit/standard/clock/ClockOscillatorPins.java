package net.logicim.domain.integratedcircuit.standard.clock;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import static net.logicim.domain.common.port.PortType.Output;

public class ClockOscillatorPins
    extends Pins
{
  protected Port output;

  public ClockOscillatorPins(VoltageConfigurationSource voltageConfiguration)
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

