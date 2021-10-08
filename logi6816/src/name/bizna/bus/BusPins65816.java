package name.bizna.bus;

import name.bizna.bus.common.Omniport;
import name.bizna.bus.common.Port;
import name.bizna.bus.common.TraceState;
import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Pins65816;

@SuppressWarnings({"FieldMayBeFinal"})
public class BusPins65816
    implements Pins65816
{
  private Cpu65816 cpu;

  protected final Omniport addressBus;
  protected final Omniport dataBus;
  protected final Port rwb;
  protected final Port clock;
  protected final Port abortB;
  protected final Port busEnable;
  protected final Port irqB;
  protected final Port nmiB;
  protected final Port resetB;
  protected final Port emulation;
  protected final Port memoryLockB;
  protected final Port mx;
  protected final Port rdy;
  protected final Port vectorPullB;
  protected final Port validProgramAddress;
  protected final Port validDataAddress;

  public BusPins65816(Omniport addressBus,
                      Omniport dataBus,
                      Port rwb,
                      Port clock,
                      Port abortB,
                      Port busEnable,
                      Port irqB,
                      Port nmiB,
                      Port resetB,
                      Port emulation,
                      Port memoryLockB,
                      Port mx,
                      Port rdy,
                      Port vectorPullB,
                      Port validProgramAddress,
                      Port validDataAddress)
  {
    this.addressBus = addressBus;
    this.dataBus = dataBus;
    this.rwb = rwb;
    this.clock = clock;
    this.abortB = abortB;
    this.rdy = rdy;
    this.busEnable = busEnable;
    this.irqB = irqB;
    this.nmiB = nmiB;
    this.resetB = resetB;
    this.emulation = emulation;
    this.memoryLockB = memoryLockB;
    this.vectorPullB = vectorPullB;
    this.validProgramAddress = validProgramAddress;
    this.validDataAddress = validDataAddress;
    this.mx = mx;
  }

  public void setCpu(Cpu65816 cpu)
  {
    this.cpu = cpu;
  }

  @Override
  public void setAddress(int address)
  {
    this.addressBus.set(address);
  }

  @Override
  public int getData()
  {
    if (cpu.isRead())
    {
      return (int) dataBus.get();
    }
    else
    {
      return 0;
    }
  }

  @Override
  public void setData(int data)
  {
    this.dataBus.set(data);
  }

  @Override
  public void setRwb(boolean rwb)
  {
    this.rwb.set(rwb);
  }

  @Override
  public boolean getPhi2()
  {
    return clock.get();
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    this.emulation.set(emulation);
  }

  @Override
  public void setMemoryLockB(boolean memoryLockB)
  {
    this.memoryLockB.set(memoryLockB);
  }

  @Override
  public void setMX(boolean mx)
  {
    this.mx.set(mx);
  }

  @Override
  public void setRdy(boolean rdy)
  {
    this.rdy.set(rdy);
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    this.vectorPullB.set(vectorPullB);
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    this.validProgramAddress.set(validProgramAddress);
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    this.validDataAddress.set(validDataAddress);
  }

  @Override
  public boolean isAbortB()
  {
    return abortB.get();
  }

  @Override
  public boolean isBusEnable()
  {
    return busEnable.get();
  }

  @Override
  public boolean isPhi2()
  {
    return clock.get();
  }

  @Override
  public boolean isIrqB()
  {
    return irqB.get();
  }

  @Override
  public boolean isNmiB()
  {
    return nmiB.get();
  }

  @Override
  public boolean isResetB()
  {
    return resetB.get();
  }
}

