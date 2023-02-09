package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class BufferPins
    extends Pins
{
  private List<LogicPort> inputs;
  private List<LogicPort> outputs;

  public BufferPins(int bufferCount, VoltageConfigurationSource voltageConfiguration)
  {
    super();

    inputs = new ArrayList<>();
    outputs = new ArrayList<>();

    for (int i = 0; i < bufferCount; i++)
    {
      LogicPort input = new LogicPort(Input,
                                      this,
                                      "Input " + i,
                                      voltageConfiguration);
      inputs.add(input);

      LogicPort output = new LogicPort(Output,
                                       this,
                                       "Output " + i,
                                       voltageConfiguration);
      outputs.add(output);
    }
  }

  public List<LogicPort> getInputs()
  {
    return inputs;
  }

  public List<LogicPort> getOutputs()
  {
    return outputs;
  }
}

