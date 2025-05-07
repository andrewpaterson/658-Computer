package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class ReadProgramBank
    extends DataOperation
{
  public ReadProgramBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getState().setNewProgramCounterBank(cpu.getData());
  }

  @Override
  public String toString()
  {
    return "Read(New_PBR)";
  }
}

