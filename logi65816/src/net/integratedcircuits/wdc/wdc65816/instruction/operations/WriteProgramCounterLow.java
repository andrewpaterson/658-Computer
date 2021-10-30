package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;
import net.util.IntUtil;

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
    setPinData(cpu, IntUtil.getLowByte(cpu.getProgramCounter().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(PCL)";
  }
}

