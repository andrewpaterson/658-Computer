package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class InternalOperation
    extends DataOperation
{
  public InternalOperation(boolean notMemoryLock)
  {
    super(false, false, notMemoryLock, true, true);
  }

  public InternalOperation(boolean validProgramAddress, boolean validDataAddress, boolean notMemoryLock)
  {
    super(validProgramAddress, validDataAddress, notMemoryLock, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getData();
  }

  @Override
  public String toString()
  {
    return "IO";
  }
}

