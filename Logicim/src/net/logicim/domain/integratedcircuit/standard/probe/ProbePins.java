package net.logicim.domain.integratedcircuit.standard.probe;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Input;

public class ProbePins
    extends Pins
{
  private List<LogicPort> inputs;

  public ProbePins(VoltageConfigurationSource voltageConfiguration, int bitWidth)
  {
    super();

    inputs = new ArrayList<>();
    for (int i = 0; i < bitWidth; i++)
    {
      LogicPort input = new LogicPort(Input,
                                      this,
                                      "Input " + i,
                                      voltageConfiguration);
      inputs.add(input);
    }
  }

  public List<LogicPort> getInputs()
  {
    return inputs;
  }
}

