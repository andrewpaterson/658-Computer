package net.wdc.wdc65816.instruction.operations;

import net.util.IntUtil;
import net.wdc.wdc65816.W65C816;

public class WriteProgramCounterHigh
    extends DataOperation
{
  public WriteProgramCounterHigh()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    setPinData(cpu, IntUtil.getHighByte(cpu.getProgramCounter().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(PCH)";
  }
}

