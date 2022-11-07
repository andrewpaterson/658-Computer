package net.logicim.domain.integratedcircuit.standard.logic.or;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.Uniport;
import net.logicim.domain.common.propagation.BistateOutputPropagation;
import net.logicim.domain.common.propagation.InputPropagation;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;
import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class OrGatePins
    extends Pins
{
  private List<Uniport> inputs;
  private Uniport output;

  public OrGatePins(int inputCount)
  {
    super();
    output = new Uniport(Output,
                         this,
                         "Output",
                         new BistateOutputPropagation("",
                                                      0.0f,
                                                      3.3f,
                                                      nanosecondsToTime(2.5f),
                                                      nanosecondsToTime(2.5f)));


    inputs = new ArrayList<>(inputCount);
    for (int i = 0; i < inputCount; i++)
    {
      Uniport uniport = new Uniport(Input,
                                    this,
                                    "Input " + i,
                                    new InputPropagation("", 0.8f, 2.0f));
      inputs.add(uniport);
    }
  }

  public Uniport getOutput()
  {
    return output;
  }

  public List<Uniport> getInputs()
  {
    return inputs;
  }
}

