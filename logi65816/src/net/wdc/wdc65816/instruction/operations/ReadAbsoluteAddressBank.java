package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.W65C816;

public class ReadAbsoluteAddressBank
    extends DataOperation
{
  public ReadAbsoluteAddressBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setAddressBank(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(AAB)";
  }
}

