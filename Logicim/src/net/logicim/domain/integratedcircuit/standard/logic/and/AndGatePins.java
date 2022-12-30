package net.logicim.domain.integratedcircuit.standard.logic.and;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class AndGatePins
    extends Pins
{
  private List<LogicPort> inputs;
  private LogicPort output;

  public AndGatePins(int inputCount, VoltageConfigurationSource voltageConfiguration)
  {
    super();
    output = new LogicPort(Output,
                           this,
                           "Output",
                           voltageConfiguration);

    inputs = new ArrayList<>(inputCount);
    for (int i = 0; i < inputCount; i++)
    {
      LogicPort port = new LogicPort(Input,
                                     this,
                                     "Input " + i,
                                     voltageConfiguration);
      inputs.add(port);
    }
  }

  public LogicPort getOutput()
  {
    return output;
  }

  public List<LogicPort> getInputs()
  {
    return inputs;
  }
}

