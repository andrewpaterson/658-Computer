package net.logicim.domain.integratedcircuit.wdc.wdc65816;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.propagation.VoltageConfigurationSource;
import net.logicim.domain.common.wire.TraceValue;

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
                                         "Data " + i,
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

  public void writeVPB(Timeline timeline, boolean b)
  {
    vpB.writeBool(timeline, b);
  }

  public TraceValue readAbort(Timeline timeline)
  {
    return abortB.readValue(timeline);
  }

  public TraceValue readIRQ(Timeline timeline)
  {
    return irqB.readValue(timeline);
  }

  public TraceValue readNMI(Timeline timeline)
  {
    return nmiB.readValue(timeline);
  }

  public void writeMLB(Timeline timeline, boolean b)
  {
    mlB.writeBool(timeline, b);
  }

  public void writeVPA(Timeline timeline, boolean b)
  {
    vpa.writeBool(timeline, b);
  }

  public void writeVDA(Timeline timeline, boolean b)
  {
    vda.writeBool(timeline, b);
  }

  public TraceValue readRES(Timeline timeline)
  {
    return resB.readValue(timeline);
  }

  public void writeMX(Timeline timeline, boolean b)
  {
    mx.writeBool(timeline, b);
  }

  public TraceValue readPhi2(Timeline timeline)
  {
    return phi2.readValue(timeline);
  }

  public TraceValue readBE(Timeline timeline)
  {
    return be.readValue(timeline);
  }

  public void writeE(Timeline timeline, boolean b)
  {
    e.writeBool(timeline, b);
  }

  public void writeRWB(Timeline timeline, boolean b)
  {
    rwb.writeBool(timeline, b);
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

