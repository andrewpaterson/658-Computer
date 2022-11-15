package net.logicim.domain.integratedcircuit.standard.logic.and;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.propagation.BistateOutputVoltage;
import net.logicim.domain.common.propagation.InputVoltage;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class AndGatePins
    extends Pins
{
  private List<Port> inputs;
  private Port output;

  public AndGatePins(int inputCount)
  {
    super();
    output = new Port(Output,
                      this,
                      "Output",
                      new BistateOutputVoltage("",
                                               0.0f,
                                               3.3f,
                                               nanosecondsToTime(2.5f),
                                               nanosecondsToTime(2.5f)));

    inputs = new ArrayList<>(inputCount);
    for (int i = 0; i < inputCount; i++)
    {
      Port port = new Port(Input,
                           this,
                           "Input " + i,
                           new InputVoltage("", 0.8f, 2.0f));
      inputs.add(port);
    }
  }

  public Port getOutput()
  {
    return output;
  }

  public List<Port> getInputs()
  {
    return inputs;
  }
}

