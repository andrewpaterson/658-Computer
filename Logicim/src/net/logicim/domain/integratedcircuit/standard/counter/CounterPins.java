package net.logicim.domain.integratedcircuit.standard.counter;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.Input;
import static net.logicim.domain.common.port.PortType.Output;

public class CounterPins
    extends Pins
{
  protected LogicPort clock;
  protected LogicPort reset;
  protected LogicPort load;
  protected List<LogicPort> data;
  protected LogicPort countEnable;
  protected LogicPort countEnableTerminal;
  protected List<LogicPort> output;
  protected LogicPort terminalOutput;

  public CounterPins(VoltageConfigurationSource voltageConfiguration, int bitWidth)
  {
    clock = new LogicPort(Input,
                          this,
                          "Clock",
                          voltageConfiguration);
    data = new ArrayList<>();
    for (int i = 0; i < bitWidth; i++)
    {
      LogicPort dataPort = new LogicPort(Input,
                                         this,
                                         "Data " + i,
                                         voltageConfiguration);
      data.add(dataPort);
    }
    reset = new LogicPort(Input,
                          this,
                          "Reset",
                          voltageConfiguration);
    load = new LogicPort(Input,
                         this,
                         "Load",
                         voltageConfiguration);
    countEnable = new LogicPort(Input,
                                this,
                                "Enable",
                                voltageConfiguration);
    countEnableTerminal = new LogicPort(Input,
                                        this,
                                        "Enable Terminal",
                                        voltageConfiguration);

    output = new ArrayList<>();
    for (int i = 0; i < bitWidth; i++)
    {
      LogicPort dataPort = new LogicPort(Output,
                                         this,
                                         "Count " + i,
                                         voltageConfiguration);
      output.add(dataPort);
    }
    terminalOutput = new LogicPort(Output,
                                   this,
                                   "Terminal Count",
                                   voltageConfiguration);
  }

  public LogicPort getClock()
  {
    return clock;
  }

  public LogicPort getReset()
  {
    return reset;
  }

  public LogicPort getLoad()
  {
    return load;
  }

  public List<LogicPort> getData()
  {
    return data;
  }

  public LogicPort getCountEnable()
  {
    return countEnable;
  }

  public LogicPort getCountEnableTerminal()
  {
    return countEnableTerminal;
  }

  public List<LogicPort> getOutput()
  {
    return output;
  }

  public LogicPort getTerminalOutput()
  {
    return terminalOutput;
  }

  public boolean isClock(LogicPort port)
  {
    return clock == port;
  }

  public int readData(Timeline timeline, int bitWidth)
  {
    int data = 0;
    for (int i = 0; i < bitWidth; i++)
    {
      data <<= 1;
      boolean bit = this.data.get(i).readValue(timeline.getTime()).isHigh();
      if (bit)
      {
        data |= 1;
      }
    }
    return data;
  }

  public void writeOutput(Timeline timeline, int data, int bitWidth)
  {
    for (int i = 0; i < bitWidth; i++)
    {
      this.output.get(i).writeBool(timeline, (data & 1) == 1);
      data >>>= 1;
    }
  }
}

