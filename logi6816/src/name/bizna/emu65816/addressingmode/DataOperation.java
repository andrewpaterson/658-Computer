package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Pins;

public abstract class DataOperation
    extends Operation
{
  protected boolean validProgramAddress;
  protected boolean validDataAddress;
  protected boolean notMemoryLock;
  protected boolean read;
  protected boolean notVectorPull;
  protected boolean ready;

  public DataOperation(boolean validProgramAddress, boolean validDataAddress, boolean notMemoryLock, boolean read, boolean notVectorPull)
  {
    this.validProgramAddress = validProgramAddress;
    this.validDataAddress = validDataAddress;
    this.notMemoryLock = notMemoryLock;
    this.read = read;
    this.notVectorPull = notVectorPull;
    this.ready = true;
  }

  @Override
  public boolean isData()
  {
    return true;
  }

  public void setPins(Cpu65816 cpu)
  {
    Pins pins = cpu.getPins();

    pins.setRead(read);
    pins.setValidDataAddress(validDataAddress);
    pins.setValidProgramAddress(validProgramAddress);
    pins.setMemoryLockB(notMemoryLock);
    pins.setVectorPullB(notVectorPull);
    pins.setReady(ready);
  }
}

