package net.logicim.domain.integratedcircuit.standard.logic.buffer;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class BufferPins
    extends Pins
{
  private LogicPort input;
  private LogicPort output;

  public BufferPins(VoltageConfigurationSource voltageConfiguration)
  {
    super();
    output = new LogicPort(Output,
                           this,
                           "Output",
                           voltageConfiguration);
    input = new LogicPort(Input,
                          this,
                          "Input",
                          voltageConfiguration);
  }

  public LogicPort getInput()
  {
    return input;
  }

  public LogicPort getOutput()
  {
    return output;
  }
}

