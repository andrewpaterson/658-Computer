package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Pins65816;

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
    Pins65816 pins = cpu.getPins();

    cpu.setRead(read);
    pins.setRwb(read);
    pins.setValidDataAddress(validDataAddress);
    pins.setValidProgramAddress(validProgramAddress);
    pins.setMemoryLockB(notMemoryLock);
    pins.setVectorPullB(notVectorPull);

    pins.setRdy(ready);
  }
}

