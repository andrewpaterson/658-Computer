package name.bizna.bus;

import name.bizna.bus.logic.ComplexTickable;
import name.bizna.cpu.Cpu65816;

public class TickableCpu
    implements ComplexTickable
{
  private final Cpu65816 cpu;
  private final BusPins65816 pins;

  protected int previousAddress;
  protected int previousData;
  protected boolean previousRWB;
  protected boolean previousEmulation;
  protected boolean previousMemoryLockB;
  protected boolean previousMX;
  protected boolean previousRDY;
  protected boolean previousVectorPullB;
  protected boolean previousValidProgramAddress;
  protected boolean previousValidDataAddress;

  public TickableCpu(BusPins65816 pins)
  {
    cpu = new Cpu65816(pins);
    this.pins = pins;
  }

  @Override
  public boolean propagate()
  {
    previousValidDataAddress = pins.validDataAddress;
    previousValidProgramAddress = pins.validProgramAddress;
    previousVectorPullB = pins.vectorPullB;
    previousRDY = pins.rdy;
    previousMX = pins.mx;
    previousMemoryLockB = pins.memoryLockB;
    previousEmulation = pins.emulation;
    previousRWB = pins.rwb;
    previousData = pins.data;
    previousAddress = pins.address;

    cpu.tick();

    boolean settled;
    settled = pins.validDataAddress == previousValidDataAddress;
    settled &= pins.validProgramAddress == previousValidProgramAddress;
    settled &= pins.vectorPullB == previousVectorPullB;
    settled &= pins.rdy == previousRDY;
    settled &= pins.mx == previousMX;
    settled &= pins.memoryLockB == previousMemoryLockB;
    settled &= pins.emulation == previousEmulation;
    settled &= pins.rwb == previousRWB;
    settled &= pins.data == previousData;
    settled &= pins.address == previousAddress;

    return settled;
  }

  public boolean isStopped()
  {
    return cpu.isStopped();
  }

  public void dump()
  {
    cpu.dump();
  }

  @Override
  public void undoInternalPropagationChange()
  {

  }
}

