package net.logicim.domain.integratedcircuit.standard.flop.dtype;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class DTypeFlipFlopPins
    extends Pins
{
  private LogicPort outputQ;
  private LogicPort outputQB;
  private LogicPort clock;
  private LogicPort data;
  private LogicPort set;
  private LogicPort reset;

  public DTypeFlipFlopPins(VoltageConfigurationSource voltageConfiguration, boolean inverseOut, boolean setReset)
  {
    outputQ = new LogicPort(Output,
                           this,
                           "Q",
                           voltageConfiguration);
    outputQB = new LogicPort(Output,
                           this,
                           "QB",
                           voltageConfiguration);

    clock = new LogicPort(Input,
                        this,
                        "CP",
                        voltageConfiguration);
    data = new LogicPort(Input,
                        this,
                        "D",
                        voltageConfiguration);
    set = new LogicPort(Input,
                        this,
                        "SD",
                        voltageConfiguration);
    reset = new LogicPort(Input,
                        this,
                        "RD",
                        voltageConfiguration);
  }

  public boolean isClock(LogicPort port)
  {
    return clock == port;
  }

  public LogicPort getOutputQ()
  {
    return outputQ;
  }

  public LogicPort getOutputQB()
  {
    return outputQB;
  }

  public LogicPort getClock()
  {
    return clock;
  }

  public LogicPort getData()
  {
    return data;
  }

  public LogicPort getSet()
  {
    return set;
  }

  public LogicPort getReset()
  {
    return reset;
  }
}

