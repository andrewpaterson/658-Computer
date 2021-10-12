package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class FetchAbsoluteAddressBank
    extends DataOperation
{
  public FetchAbsoluteAddressBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setAddressBank(cpu.getPins().getData());
  }

  @Override
  public String toString()
  {
    return "Read(AAB)";
  }
}

