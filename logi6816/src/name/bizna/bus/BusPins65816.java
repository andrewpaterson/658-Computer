package name.bizna.bus;

import name.bizna.bus.common.Omnibus;
import name.bizna.bus.common.Single;
import name.bizna.cpu.Pins65816;

@SuppressWarnings({"FieldMayBeFinal"})
public class BusPins65816
    implements Pins65816
{
  protected final Omnibus addressBus;
  protected final Omnibus dataBus;
  protected final Single rwbTrace;
  protected final Single clockTrace;
  protected final Single abortBTrace;
  protected final Single busEnableTrace;
  protected final Single irqBTrace;
  protected final Single nmiBTrace;
  protected final Single resetBTrace;
  protected final Single emulationTrace;
  protected final Single memoryLockBTrace;
  protected final Single mxTrace;
  protected final Single rdyTrace;
  protected final Single vectorPullBTrace;
  protected final Single validProgramAddressTrace;
  protected final Single validDataAddressTrace;

  protected int address;                  //output
  protected int data;                     //bi-directional
  protected boolean rwb;                  //output
  protected boolean emulation;            //output
  protected boolean memoryLockB;          //output
  protected boolean mx;                   //output
  protected boolean rdy;                  //bi-directional.  Also.  Fuck-it I'm treating this an output.
  protected boolean vectorPullB;          //output
  protected boolean validProgramAddress;  //output
  protected boolean validDataAddress;     //output

  public BusPins65816(Omnibus addressBus,
                      Omnibus dataBus,
                      Single rwbTrace,
                      Single clockTrace,
                      Single abortBTrace,
                      Single busEnable,
                      Single irqBTrace,
                      Single nmiBTrace,
                      Single resetBTrace,
                      Single emulationTrace,
                      Single memoryLockBTrace,
                      Single mxTrace,
                      Single rdyTrace,
                      Single vectorPullBTrace,
                      Single validProgramAddressTrace,
                      Single validDataAddressTrace)
  {
    this.addressBus = addressBus;
    this.dataBus = dataBus;
    this.rwbTrace = rwbTrace;
    this.clockTrace = clockTrace;
    this.abortBTrace = abortBTrace;
    this.rdyTrace = rdyTrace;
    this.busEnableTrace = busEnable;
    this.irqBTrace = irqBTrace;
    this.nmiBTrace = nmiBTrace;
    this.resetBTrace = resetBTrace;
    this.emulationTrace = emulationTrace;
    this.memoryLockBTrace = memoryLockBTrace;
    this.vectorPullBTrace = vectorPullBTrace;
    this.validProgramAddressTrace = validProgramAddressTrace;
    this.validDataAddressTrace = validDataAddressTrace;
    this.mxTrace = mxTrace;

    this.address = addressBus.get();
    this.data = dataBus.get();
    this.rwb = rwbTrace.get();
    this.emulation = emulationTrace.get();
    this.memoryLockB = memoryLockBTrace.get();
    this.mx = mxTrace.get();
    this.rdy = rdyTrace.get();
    this.vectorPullB = vectorPullBTrace.get();
    this.validProgramAddress = validProgramAddressTrace.get();
    this.validDataAddress = validDataAddressTrace.get();
  }

  @Override
  public void setAddress(int address)
  {
    this.address = address;
    this.addressBus.set(address);
  }

  @Override
  public int getData()
  {
    if (rwb)
    {
      return dataBus.get();
    }
    else
    {
      return data;
    }
  }

  @Override
  public void setData(int data)
  {
    this.data = data;
    this.dataBus.set(data);
  }

  @Override
  public void setRwb(boolean rwb)
  {
    this.rwbTrace.set(rwb);
    this.rwb = rwb;
  }

  @Override
  public boolean getPhi2()
  {
    return clockTrace.get();
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    this.emulation = emulation;
    this.emulationTrace.set(emulation);
  }

  @Override
  public void setMemoryLockB(boolean memoryLockB)
  {
    this.memoryLockB = memoryLockB;
    this.memoryLockBTrace.set(memoryLockB);
  }

  @Override
  public void setMX(boolean mx)
  {
    this.mx = mx;
    this.mxTrace.set(mx);
  }

  @Override
  public void setRdy(boolean rdy)
  {
    this.rdy = rdy;
    this.rdyTrace.set(rdy);
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    this.vectorPullB = vectorPullB;
    this.vectorPullBTrace.set(vectorPullB);
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    this.validProgramAddress = validProgramAddress;
    this.validProgramAddressTrace.set(validProgramAddress);
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    this.validDataAddress = validDataAddress;
    this.validDataAddressTrace.set(validDataAddress);
  }

  @Override
  public boolean isAbortB()
  {
    return abortBTrace.get();
  }

  @Override
  public boolean isBusEnable()
  {
    return busEnableTrace.get();
  }

  @Override
  public boolean isPhi2()
  {
    return clockTrace.get();
  }

  @Override
  public boolean isIrqB()
  {
    return irqBTrace.get();
  }

  @Override
  public boolean isNmiB()
  {
    return nmiBTrace.get();
  }

  @Override
  public boolean isResetB()
  {
    return resetBTrace.get();
  }
}

