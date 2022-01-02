package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class ReadDataBank
    extends DataOperation
{
  public ReadDataBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setDataBank(cpu.getData());
  }

  @Override
  public String toString()
  {
    return "Read(DBR)";
  }
}

