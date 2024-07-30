package net.logicim.domain.integratedcircuit.wdc.wdc65816;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;

import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.port.PortType.*;

public class W65C816Pins
    extends Pins
{
  protected List<LogicPort> address;
  protected List<LogicPort> data;
  protected LogicPort vpB;
  protected LogicPort rdy;
  protected LogicPort abortB;
  protected LogicPort irqB;
  protected LogicPort nmiB;
  protected LogicPort mlB;
  protected LogicPort vpa;
  protected LogicPort vda;
  protected LogicPort resB;
  protected LogicPort mx;
  protected LogicPort phi2;
  protected LogicPort be;
  protected LogicPort e;
  protected LogicPort rwb;

  public W65C816Pins(VoltageConfigurationSource voltageConfiguration)
  {
    super();

    address = new ArrayList<>();
    for (int i = 0; i < 16; i++)
    {
      LogicPort addressPort = new LogicPort(Output,
                                            this,
                                            "Address " + i,
                                            voltageConfiguration);
      address.add(addressPort);
    }

    data = new ArrayList<>();
    for (int i = 0; i < 8; i++)
    {
      LogicPort dataPort = new LogicPort(Bidirectional,
                                         this,
                                         "Address " + i,
                                         voltageConfiguration);
      data.add(dataPort);
    }

    vpB = new LogicPort(Output,
                        this,
                        "VPB",
                        voltageConfiguration);

    rdy = new LogicPort(Input,
                        this,
                        "RDY",
                        voltageConfiguration);

    abortB = new LogicPort(Input,
                           this,
                           "ABORTB",
                           voltageConfiguration);

    irqB = new LogicPort(Input,
                         this,
                         "IRQB",
                         voltageConfiguration);

    nmiB = new LogicPort(Input,
                         this,
                         "NMIB",
                         voltageConfiguration);

    mlB = new LogicPort(Output,
                        this,
                        "MLB",
                        voltageConfiguration);

    vpa = new LogicPort(Output,
                        this,
                        "VPA",
                        voltageConfiguration);

    vda = new LogicPort(Output,
                        this,
                        "VDA",
                        voltageConfiguration);

    resB = new LogicPort(Input,
                         this,
                         "RESB",
                         voltageConfiguration);

    mx = new LogicPort(Output,
                       this,
                       "MX",
                       voltageConfiguration);

    phi2 = new LogicPort(Input,
                         this,
                         "PHI2",
                         voltageConfiguration);

    be = new LogicPort(Input,
                       this,
                       "BE",
                       voltageConfiguration);

    e = new LogicPort(Output,
                      this,
                      "E",
                      voltageConfiguration);

    rwb = new LogicPort(Output,
                        this,
                        "RWB",
                        voltageConfiguration);
  }

  public List<LogicPort> getAddress()
  {
    return address;
  }

  public List<LogicPort> getData()
  {
    return data;
  }

  public LogicPort getVpB()
  {
    return vpB;
  }

  public LogicPort getRdy()
  {
    return rdy;
  }

  public LogicPort getAbortB()
  {
    return abortB;
  }

  public LogicPort getIrqB()
  {
    return irqB;
  }

  public LogicPort getNmiB()
  {
    return nmiB;
  }

  public LogicPort getMlB()
  {
    return mlB;
  }

  public LogicPort getVpa()
  {
    return vpa;
  }

  public LogicPort getVda()
  {
    return vda;
  }

  public LogicPort getResB()
  {
    return resB;
  }

  public LogicPort getMx()
  {
    return mx;
  }

  public LogicPort getPhi2()
  {
    return phi2;
  }

  public LogicPort getBe()
  {
    return be;
  }

  public LogicPort getE()
  {
    return e;
  }

  public LogicPort getRwb()
  {
    return rwb;
  }

  public void writeAddress(Timeline timeline, int address)
  {
    for (int i = 0; i < 16; i++)
    {
      this.address.get(i).writeBool(timeline, (address & 1) == 1);
      address >>>= 1;
    }
  }

  public void writeData(Timeline timeline, int bank)
  {
    for (int i = 0; i < 8; i++)
    {
      this.data.get(i).writeBool(timeline, (bank & 1) == 1);
      bank >>>= 1;
    }
  }

  public int readData(Timeline timeline)
  {
    int data = 0;
    for (int i = 0; i < 8; i++)
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
}

