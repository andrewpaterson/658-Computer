package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.common.util.IntUtil;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class WriteProgramCounterLow
    extends DataOperation
{
  public WriteProgramCounterLow()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getState().writeProgramCounterLow();
  }

  @Override
  public String toString()
  {
    return "Write(PCL)";
  }
}

