package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchAbsoluteAddressLow
    extends DataOperation
{
  public FetchAbsoluteAddressLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setAddressLow(cpu.getPinData());
  }
}

