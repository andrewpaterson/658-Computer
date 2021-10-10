package name.bizna.bus.cpu;

import name.bizna.bus.common.*;
import name.bizna.bus.logic.Tickable;
import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.CpuSnapshot;
import name.bizna.cpu.Pins65816;
import name.bizna.util.EmulatorException;

@SuppressWarnings({"FieldMayBeFinal"})
public class TickablePins65816
    extends Tickable
    implements Pins65816
{
  private Cpu65816 cpu;
  private CpuSnapshot snapshot;

  protected final Omniport addressBus;
  protected final Omniport dataBus;
  protected final Uniport rwb;
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

  public TickablePins65816(Tickables tickables,
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
    this.rwb = new Uniport(this, "RWB");
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
    this.rwb.connect(rwb);
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
  public void propagate()
  {
    TraceValue clockValue = clock.readState();
    TraceValue abortBValue = abortB.readState();
    TraceValue busEnableValue = busEnable.readState();
    TraceValue irqbValue = irqB.readState();
    TraceValue nmibValue = nmiB.readState();
    TraceValue resetBValue = resetB.readState();

    boolean rwbState = rwb.writeState();
    boolean addressState = addressBus.writeState();
    boolean rdyState = rdy.writeState();
    boolean emulationState = emulation.writeState();
    boolean memoryLockBState = memoryLockB.writeState();
    boolean vectorPullBState = vectorPullB.writeState();
    boolean validProgramAddressState = validProgramAddress.writeState();
    boolean validDataAddressState = validDataAddress.writeState();
    boolean mxState = mx.writeState();

    if (clockValue.isValid() &&
        abortBValue.isValidOrUndefined() &&
        busEnableValue.isValidOrUndefined() &&
        irqbValue.isValidOrUndefined() &&
        nmibValue.isValidOrUndefined() &&
        resetBValue.isValidOrUndefined())
    {
      cpu.tick();
    }
    else if (clockValue.isError() ||
             abortBValue.isError() ||
             busEnableValue.isError() ||
             irqbValue.isError() ||
             nmibValue.isError() ||
             resetBValue.isError())
    {
      writeAllPinsError();
    }
    else if (clockValue.isUndefined())
    {
      writeAllPinsUndefined();
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

  public void setCpu(Cpu65816 cpu)
  {
    this.cpu = cpu;
  }

  @Override
  public void setAddress(int address)
  {
    this.addressBus.writeAllPinsBool(address);
  }

  @Override
  public int getData()
  {
    if (cpu.isRead())
    {
      TraceValue traceValue = dataBus.readStates();
      if (traceValue.isValid())
      {
        return (int) dataBus.getPinsAsBoolAfterRead();
      }
      else if (traceValue.isError())
      {
        writeAllPinsError();
        return 0;
      }
      else if (traceValue.isUndefined())
      {
        writeAllPinsUndefined();
        return 0;
      }
      else
      {
        throw new EmulatorException("Cannot getData from pins in unknown state.");
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
    this.dataBus.writeAllPinsBool(data);
  }

  @Override
  public void setRwb(boolean rwb)
  {
    this.rwb.writeBool(rwb);
  }

  @Override
  public boolean getPhi2()
  {
    return clock.readBool();
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
  public void setMX(boolean mx)
  {
    this.mx.writeBool(mx);
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
  public boolean isAbortB()
  {
    return abortB.readBool();
  }

  @Override
  public boolean isBusEnable()
  {
    return busEnable.readBool();
  }

  @Override
  public boolean isIrqB()
  {
    return irqB.readBool();
  }

  @Override
  public boolean isNmiB()
  {
    return nmiB.readBool();
  }

  @Override
  public boolean isResetB()
  {
    return resetB.readBool();
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

  @Override
  public void undoPropagation()
  {
    cpu.restoreCpuFromSnapshot(snapshot);
  }

  @Override
  public void writeAllPinsUndefined()
  {
    rwb.writeState(TraceValue.Undefined);
    addressBus.writeAllPinsUndefined();
    dataBus.writeAllPinsUndefined();
    rdy.writeState(TraceValue.Undefined);
    emulation.writeState(TraceValue.Undefined);
    memoryLockB.writeState(TraceValue.Undefined);
    vectorPullB.writeState(TraceValue.Undefined);
    validProgramAddress.writeState(TraceValue.Undefined);
    validDataAddress.writeState(TraceValue.Undefined);
    mx.writeState(TraceValue.Undefined);
  }

  @Override
  public void writeAllPinsError()
  {
    rwb.writeState(TraceValue.Error);
    addressBus.writeAllPinsError();
    dataBus.writeAllPinsError();
    rdy.writeState(TraceValue.Error);
    emulation.writeState(TraceValue.Error);
    memoryLockB.writeState(TraceValue.Error);
    vectorPullB.writeState(TraceValue.Error);
    validProgramAddress.writeState(TraceValue.Error);
    validDataAddress.writeState(TraceValue.Error);
    mx.writeState(TraceValue.Error);
  }
}

