package net.logicim.domain.integratedcircuit.standard.constant;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Output;

public class ConstantPins
    extends Pins
{
  private List<LogicPort> outputs;

  public ConstantPins(VoltageConfigurationSource voltageConfiguration, int bitWidth)
  {
    super();
    outputs = new ArrayList<>();
    for (int i = 0; i < bitWidth; i++)
    {
      LogicPort input = new LogicPort(Output,
                                      this,
                                      "Output " + i,
                                      voltageConfiguration);
      outputs.add(input);
    }
  }

  public List<LogicPort> getOutputs()
  {
    return outputs;
  }
}

