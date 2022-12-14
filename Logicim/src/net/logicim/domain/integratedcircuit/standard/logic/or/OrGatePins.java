package net.logicim.domain.integratedcircuit.standard.logic.or;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.port.VoltageCommon;
import net.logicim.domain.common.port.VoltageGround;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class OrGatePins
    extends Pins
{
  private List<Port> inputs;
  private Port output;

  public OrGatePins(int inputCount, VoltageConfigurationSource voltageConfiguration)
  {
    super();
    output = new Port(Output,
                      this,
                      "Output",
                      voltageConfiguration);

    inputs = new ArrayList<>(inputCount);
    for (int i = 0; i < inputCount; i++)
    {
      Port port = new Port(Input,
                           this,
                           "Input " + i,
                           voltageConfiguration);
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

  public Port getInput(int i)
  {
    return inputs.get(i);
  }
}

