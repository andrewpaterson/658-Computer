package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class ReadDataBank
    extends DataOperation
{
  public ReadDataBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setDataBank(cpu.getPins().getData());
  }

  @Override
  public String toString()
  {
    return "Read(DBR)";
  }
}

