package net.wdc.simulation;

import net.simulation.common.*;
import net.util.EmulatorException;
import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.WDC65816Pins;
import net.wdc.wdc65816.WDC65816Snapshot;

public class WDC65816TickablePins
    extends Tickable
    implements WDC65816Pins
{
  private WDC65816 cpu;
  private WDC65816Snapshot snapshot;

  protected final Omniport addressBus;
  protected final Omniport dataBus;
  protected final Uniport rwB;
  protected final Uniport clock;
  protected final Uniport abortB;
  protected final Uniport busEnable;
  protected final Uniport irqB;
  protected final Uniport nmiB;
  protected final Uniport resetB;
  protected final Uniport emulation;
  protected final Uniport memoryLockB;
  protected final Uniport mx;
  protected final Uniport rdy;
  protected final Uniport vectorPullB;
  protected final Uniport validProgramAddress;
  protected final Uniport validDataAddress;

  public WDC65816TickablePins(Tickables tickables,
                              String name,
                              Bus addressBus,
                              Bus dataBus,
                              Trace rwb,
                              Trace clock,
                              Trace abortB,
                              Trace busEnable,
                              Trace irqB,
                              Trace nmiB,
                              Trace resetB,
                              Trace emulation,
                              Trace memoryLockB,
                              Trace mx,
                              Trace rdy,
                              Trace vectorPullB,
                              Trace validProgramAddress,
                              Trace validDataAddress)
  {
    super(tickables, name);
    this.addressBus = new Omniport(this, "Address Bus", 16);
    this.dataBus = new Omniport(this, "Data Bus", 8);
    this.rwB = new Uniport(this, "RWB");
    this.clock = new Uniport(this, "Phi2");
    this.abortB = new Uniport(this, "AbortB");
    this.rdy = new Uniport(this, "Rdy");
    this.busEnable = new Uniport(this, "BE");
    this.irqB = new Uniport(this, "IRQB");
    this.nmiB = new Uniport(this, "NMIB");
    this.resetB = new Uniport(this, "ResB");
    this.emulation = new Uniport(this, "E");
    this.memoryLockB = new Uniport(this, "MLB");
    this.vectorPullB = new Uniport(this, "VPB");
    this.validProgramAddress = new Uniport(this, "VPA");
    this.validDataAddress = new Uniport(this, "VDA");
    this.mx = new Uniport(this, "MX");

    this.addressBus.connect(addressBus);
    this.dataBus.connect(dataBus);
    this.rwB.connect(rwb);
    this.clock.connect(clock);
    this.abortB.connect(abortB);
    this.rdy.connect(rdy);
    this.busEnable.connect(busEnable);
    this.irqB.connect(irqB);
    this.nmiB.connect(nmiB);
    this.resetB.connect(resetB);
    this.emulation.connect(emulation);
    this.memoryLockB.connect(memoryLockB);
    this.vectorPullB.connect(vectorPullB);
    this.validProgramAddress.connect(validProgramAddress);
    this.validDataAddress.connect(validDataAddress);
    this.mx.connect(mx);

    snapshot = null;
  }

  @Override
  public void startPropagation()
  {
    snapshot = cpu.createCpuSnapshot();
  }

  @Override
  public void donePropagation()
  {
    snapshot = null;
  }

  public void undoPropagation()
  {
    if (snapshot != null)
    {
      cpu.restoreCpuFromSnapshot(snapshot);
    }
  }

  @Override
  public void propagate()
  {
    undoPropagation();

    TraceValue clockValue = clock.read();
    TraceValue abortBValue = abortB.read();
    TraceValue busEnableValue = busEnable.read();
    TraceValue irqbValue = irqB.read();
    TraceValue nmibValue = nmiB.read();
    TraceValue resetBValue = resetB.read();

    if (clockValue.isValid() &&
        abortBValue.isValidOrUndefined() &&
        busEnableValue.isValidOrUndefined() &&
        irqbValue.isValidOrUndefined() &&
        nmibValue.isValidOrUndefined() &&
        resetBValue.isValidOrUndefined())
    {
      cpu.preTick(clock.getBoolAfterRead());
      cpu.tick();
    }
    else if (clockValue.isError() ||
             abortBValue.isError() ||
             busEnableValue.isError() ||
             irqbValue.isError() ||
             nmibValue.isError() ||
             resetBValue.isError())
    {
      setAllOutputsError();
    }
    else if (clockValue.isUnsettled())
    {
      setAllOutputsUnknown();
    }
    else
    {
      throw new EmulatorException("Cannot tick CPU with pins in unexpected state combination.");
    }
  }

  @Override
  public String getType()
  {
    return "WDC65C816";
  }

  public void setCpu(WDC65816 cpu)
  {
    this.cpu = cpu;
  }

  @Override
  public void disableBusses()
  {
    this.addressBus.highImpedance();
    this.dataBus.highImpedance();
    this.rwB.highImpedance();
  }

  @Override
  public void setAddress(int address)
  {
    if (cpu.isBusEnable())
    {
      this.addressBus.writeAllPinsBool(address);
    }
  }

  @Override
  public int getData()
  {
    if (cpu.isRead())
    {
      if (cpu.isBusEnable())
      {
        TraceValue traceValue = dataBus.read();
        if (traceValue.isValid())
        {
          return (int) dataBus.getPinsAsBoolAfterRead();
        }
        else if (traceValue.isError())
        {
          setAllOutputsError();
          return 0;
        }
        else if (traceValue.isUnsettled())
        {
          return 0;
        }
        else
        {
          throw new EmulatorException("Cannot getData from pins in unknown state.");
        }
      }
      else
      {
        return 0;
      }
    }
    else
    {
      throw new EmulatorException("Cannot getData from pins in Write Mode.");
    }
  }

  @Override
  public void setData(int data)
  {
    if (cpu.isBusEnable())
    {
      this.dataBus.writeAllPinsBool(data);
    }
  }

  @Override
  public void setBank(int data)
  {
    if (cpu.isBusEnable())
    {
      this.dataBus.writeAllPinsBool(data);
    }
  }

  @Override
  public void setRWB(boolean rwB)
  {
    if (cpu.isBusEnable())
    {
      this.rwB.writeBool(rwB);
    }
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    this.emulation.writeBool(emulation);
  }

  @Override
  public void setMemoryLockB(boolean memoryLockB)
  {
    this.memoryLockB.writeBool(memoryLockB);
  }

  @Override
  public void setMX(boolean m)
  {
    this.mx.writeBool(m);
  }

  @Override
  public void setRdy(boolean rdy)
  {
    this.rdy.writeBool(rdy);
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    this.vectorPullB.writeBool(vectorPullB);
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    this.validProgramAddress.writeBool(validProgramAddress);
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    this.validDataAddress.writeBool(validDataAddress);
  }

  @Override
  public boolean isBusEnable()
  {
    return busEnable.getBoolAfterRead();
  }

  @Override
  public boolean isAbort()
  {
    return !abortB.getBoolAfterRead();
  }

  @Override
  public boolean isReset()
  {
    return !resetB.getBoolAfterRead();
  }

  @Override
  public boolean isIRQ()
  {
    return !irqB.getBoolAfterRead();
  }

  @Override
  public boolean isNMI()
  {
    return !nmiB.getBoolAfterRead();
  }

  public void setAllOutputsUnknown()
  {
    if (cpu.isBusEnable())
    {
      addressBus.unset();
      dataBus.unset();
      rwB.unset();
    }
    rdy.unset();
    emulation.unset();
    memoryLockB.unset();
    vectorPullB.unset();
    validProgramAddress.unset();
    validDataAddress.unset();
    mx.unset();
  }

  public void setAllOutputsError()
  {
    if (cpu.isBusEnable())
    {
      addressBus.error();
      dataBus.error();
      rwB.error();
    }
    rdy.error();
    emulation.error();
    memoryLockB.error();
    vectorPullB.error();
    validProgramAddress.error();
    validDataAddress.error();
    mx.error();
  }

  public Omniport getAddressBus()
  {
    return addressBus;
  }

  public Omniport getDataBus()
  {
    return dataBus;
  }

  public Uniport getRwB()
  {
    return rwB;
  }

  public Uniport getVectorPullB()
  {
    return vectorPullB;
  }

  public Uniport getValidProgramAddress()
  {
    return validProgramAddress;
  }

  public Uniport getValidDataAddress()
  {
    return validDataAddress;
  }

  public Uniport getClock()
  {
    return clock;
  }

  public Uniport getAbortB()
  {
    return abortB;
  }

  public Uniport getBusEnable()
  {
    return busEnable;
  }

  public Uniport getIrqB()
  {
    return irqB;
  }

  public Uniport getNmiB()
  {
    return nmiB;
  }

  public Uniport getResetB()
  {
    return resetB;
  }

  public Uniport getEmulation()
  {
    return emulation;
  }

  public Uniport getMemoryLockB()
  {
    return memoryLockB;
  }

  public Uniport getMx()
  {
    return mx;
  }

  public Uniport getRdy()
  {
    return rdy;
  }

  public WDC65816 getCpu()
  {
    return cpu;
  }
}

